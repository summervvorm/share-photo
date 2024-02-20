package com.example.photos.strategy.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.photos.entity.Tag;
import com.example.photos.entity.UserImageActions;
import com.example.photos.mapper.UserImageActionsMapper;
import com.example.photos.model.dto.PictureInfoDTO;
import com.example.photos.service.RedisService;
import com.example.photos.strategy.RecommendStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Auther: raolongxiang
 * @Date: 2024/1/24
 * @Description: com.example.photos.strategy.impl
 */
@Service("contentBaseStrategyImpl")
@Slf4j
public class ContentBaseStrategyImpl implements RecommendStrategy {


    @Autowired
    RedisService redisService;

    @Autowired
    UserImageActionsMapper userImageActionsMapper;

    @Override
    public List<PictureInfoDTO> getRecommendPic(Integer userId) {
        /* todo 基于内容个性化推荐，需不需要放缓存呢？已经推荐过的图片怎么避免重复推荐呢？(使用redis储存id列表然后去重？)
         *  用户画像也放到redis中,每次登陆就更新,然后在用户有重要行为操作时（如喜欢、收藏、下载等）
         *  立即更新用户画像，而对于较小的行为（如浏览、点击等），采用延迟更新。
         *  对于图片向量的话不需要去更新，直接计算即可。
         */
//        根据用户向量去获取与用户向量相似的图片集然后根据占比去限制每个向量的数量，并且去重，最后获取基本固定数量的图片集。
        Map<Integer, Map<String, Double>> picFeature = getPicFeature(userId);
        Map<String, Double> userFeature = getUserFeature(userId);
        List<Integer> picIds = new ArrayList<>(picFeature.keySet());

        List<Integer> userIds = userImageActionsMapper.selectList(Wrappers.<UserImageActions>lambdaQuery().
                eq(UserImageActions::getUserId, userId).ge(UserImageActions::getCreateTime, LocalDateTime.now()
                        .minusMonths(1))).stream().map(UserImageActions::getPicId).collect(Collectors.toList());
        //去除以操作过的图片集 todo 记录几分钟内对用户推荐过的图片，然后去除这个图片集
        List<Integer> recommendedPicIds = (List<Integer>) redisService.get("recommendPicIds:" + userId);
        if (recommendedPicIds != null)
            picIds.removeAll(recommendedPicIds);
        picIds.removeAll(userIds);

        Collections.sort(picIds, (id1, id2) ->
                Double.compare(getSimilarity(picFeature.get(id2), userFeature),
                        getSimilarity(picFeature.get(id1), userFeature))
        );
        if(picIds.size()>30)
        picIds.removeAll(picIds.subList(30, picIds.size()));

        List<Map<String, Object>> list = userImageActionsMapper.getPicInfoByList(picIds);
        Map<Integer, PictureInfoDTO> res = new HashMap<>();
        list.forEach(map -> {
            Integer picId = (Integer) map.get("pic_id");
            PictureInfoDTO pictureInfoDTO = res.getOrDefault(picId, new PictureInfoDTO());
            pictureInfoDTO.setPicId(picId);
            pictureInfoDTO.setPicName((String) map.get("pic_name"));
            pictureInfoDTO.setPicCollections(Integer.parseInt(map.get("pic_collections").toString()));
            pictureInfoDTO.setPicLikes(Integer.parseInt(map.get("pic_likes").toString()));
            pictureInfoDTO.setColor((String) map.get("pic_color"));
            pictureInfoDTO.setPicSize(Long.parseLong(map.get("size").toString()));
            pictureInfoDTO.setPicWidth((Integer) map.get("width"));
            pictureInfoDTO.setPicHeight((Integer) map.get("height"));
            pictureInfoDTO.setStatus((Integer) map.get("status"));
            pictureInfoDTO.setPicSign((String) map.get("pic_sign"));
            pictureInfoDTO.setPicCreatTime((LocalDateTime) map.get("pic_creat_time"));
            pictureInfoDTO.setUserId((Integer) map.get("pic_by_id"));
            if (pictureInfoDTO.getTags() == null) {
                pictureInfoDTO.setTags(new ArrayList<>());
            }
            pictureInfoDTO.getTags().add(new Tag((Integer) map.get("tag_id"), (String) map.get("tag_name"), null));
            res.put((Integer) map.get("pic_id"), pictureInfoDTO);
        });

        List<PictureInfoDTO> pictureInfoDTOS = new ArrayList<>(res.values());

        List<Integer> recommendPicId = pictureInfoDTOS.stream().map(PictureInfoDTO::getPicId).collect(Collectors.toList());
        if (recommendedPicIds != null)
            recommendPicId.addAll(recommendPicId);
        redisService.set("recommendPicIds:" + userId, recommendPicId, 60 * 3);

        return pictureInfoDTOS;
    }

    private Map<String, Double> getUserFeature(Integer userId) {

        return (Map<String, Double>) redisService.get("userFeature:" + userId);

    }


    public Map<Integer, Map<String, Double>> getPicFeature(Integer userId) {
        Map<String, Double> userFeature = getUserFeature(userId);

        if (userFeature == null) {
            return null;
        }
        Map<Integer, Map<String, Double>> picFeature = new HashMap<>();
        List<String> feature = new ArrayList<>(userFeature.keySet());
        //根据用户向量的特征去获取图片列表，然后计算这些图片的向量。用户向量由以下组成：tagName
        List<Map<String, Object>> mapList = userImageActionsMapper.getPicFeatureByList(feature);
        mapList.forEach(map -> {
            Integer id = (Integer) map.get("pic_id");
            Map<String, Double> map1 = picFeature.getOrDefault(id, new HashMap<>());
            map1.put((String) map.get("tag_name"), map1.getOrDefault(map.get("tag_name"), 1.0));
            picFeature.put(id, map1);
        });

        return picFeature;
    }

    private Double getSimilarity(Map<String, Double> picFeature, Map<String, Double> userFeature) {
        RealVector vector1 = toRealVector(picFeature, userFeature);
        RealVector vector2 = toRealVector(userFeature, userFeature);
        // 计算余弦相似度
        double dotProduct = vector1.dotProduct(vector2);
        double normA = vector1.getNorm();
        double normB = vector2.getNorm();
        return dotProduct / (normA * normB);
    }

    private RealVector toRealVector(Map<String, Double> map, Map<String, Double> userFeature) {
        RealVector vector = new ArrayRealVector(userFeature.size());
        int i = 0;
        for (String str : userFeature.keySet()) {
            vector.setEntry(i, map.getOrDefault(str, 0.0));
            i++;
        }
        return vector;
    }

}

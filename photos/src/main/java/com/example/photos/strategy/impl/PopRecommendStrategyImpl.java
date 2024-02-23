package com.example.photos.strategy.impl;

import com.example.photos.entity.Tag;
import com.example.photos.mapper.UserImageActionsMapper;
import com.example.photos.model.dto.PictureInfoDTO;
import com.example.photos.service.RedisService;
import com.example.photos.strategy.RecommendStrategy;
import com.example.photos.util.BloomFilterUtil;
import org.redisson.api.RBloomFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.photos.constant.CommendConstant.COMMENDED_PIC_LIST;

/**
 * @Auther: raolongxiang
 * @Date: 2024/1/24
 * @Description: com.example.photos.strategy.impl
 */
@Service("popRecommendStrategyImpl")
public class PopRecommendStrategyImpl implements RecommendStrategy {

    @Autowired
    RedisService redisService;
    @Autowired
    UserImageActionsMapper actionsMapper;

    @Autowired
    BloomFilterUtil bloomFilterUtil;


    @Override
    public List<PictureInfoDTO> getRecommendPic(Integer userId) {

        List<PictureInfoDTO> pictureInfoDTOS;

        List<Map<String, Object>> list = actionsMapper.getPopRecommendPic();
        pictureInfoDTOS = new ArrayList<>(list.stream().collect(Collectors.groupingBy(
                map -> (Integer) map.get("pic_id"),
                Collectors.collectingAndThen(
                        Collectors.mapping(
                                map -> new Tag((Integer) map.get("tag_id"), (String) map.get("tag_name"), null),
                                Collectors.toList()
                        ),
                        tags -> {
                            Map<String, Object> firstMap = list.get(0);
                            PictureInfoDTO picInfo = new PictureInfoDTO();
                            picInfo.setPicId((Integer) firstMap.get("pic_id"));
                            picInfo.setPicName((String) firstMap.get("pic_name"));
                            picInfo.setPicCollections(Integer.parseInt(firstMap.get("pic_collections").toString()));
                            picInfo.setPicLikes(Integer.parseInt(firstMap.get("pic_likes").toString()));
                            picInfo.setColor((String) firstMap.get("pic_color"));
                            picInfo.setPicSize(Long.parseLong(firstMap.get("size").toString()));
                            picInfo.setPicWidth((Integer) firstMap.get("width"));
                            picInfo.setPicHeight((Integer) firstMap.get("height"));
                            picInfo.setStatus((Integer) firstMap.get("status"));
                            picInfo.setPicSign((String) firstMap.get("pic_sign"));
                            picInfo.setPicCreatTime((LocalDateTime) firstMap.get("pic_creat_time"));
                            picInfo.setUserId((Integer) firstMap.get("pic_by_id"));
                            picInfo.setTags(tags);
                            return picInfo;
                        }
                )
        )).values());
        pictureInfoDTOS.forEach(System.out::println);


        return pictureInfoDTOS;
    }

    @Override
    public void filterRecommendPic(List<PictureInfoDTO> pictureInfoDTOS,Integer userId) {

        long expectedInsertions = 10000L;
        // 错误比率
        double falseProbability = 0.01;

        RBloomFilter<String> bloomFilter = bloomFilterUtil.create(COMMENDED_PIC_LIST, expectedInsertions, falseProbability);
        if (pictureInfoDTOS != null) {
            for (PictureInfoDTO pictureInfoDTO : pictureInfoDTOS) {

                bloomFilter.add(pictureInfoDTO.getPicId() + ":" + userId);

            }
        }


    }
}

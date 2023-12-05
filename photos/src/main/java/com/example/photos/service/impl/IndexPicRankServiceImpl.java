package com.example.photos.service.impl;

import com.example.photos.entity.Picture;
import com.example.photos.entity.Tag;
import com.example.photos.exception.CommonJsonException;
import com.example.photos.mapper.PictureMapper;
import com.example.photos.model.dto.PictureInfoDTO;
import com.example.photos.service.IndexPicRankService;
import com.example.photos.service.PictureTagService;
import com.example.photos.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.photos.constant.RedisConstant.*;

/**
 * @Auther: Arrow
 * @Date: 2023/6/7
 * @Description: com.example.photos.service.impl
 */
@Service
@Slf4j
public class IndexPicRankServiceImpl implements IndexPicRankService {

    @Resource
    PictureMapper pictureMapper;

    @Resource
    PictureTagService pictureTagService;

    @Resource
    RedisService redisService;
    @Override
    public List<PictureInfoDTO> getHotPictures() {

        return (List<PictureInfoDTO>) redisService.get(HOT_PICTURE);
    }

    @Override
    public List<PictureInfoDTO> getNewPictures() {

        return (List<PictureInfoDTO>) redisService.get(NEW_PICTURE);
    }

    @Override
    public void setHotPictures() {
        List<Picture> pictures = pictureMapper.selectPicturesByHot();
        Map<Integer,List<Tag>> lists = new HashMap<>(pictureTagService.getTagsByPicList(pictures));
        List<PictureInfoDTO> pictureInfoDTOS = new ArrayList<>();
        if(pictures == null || lists == null){
            throw new CommonJsonException("无图片或图片信息为空");
        }

        for(int i=0;i<pictures.size();i++){
            PictureInfoDTO pictureInfoDTO = new PictureInfoDTO(pictures.get(i),lists.get(pictures.get(i).getPicId()));
            pictureInfoDTOS.add(pictureInfoDTO);
        }
        log.info(pictureInfoDTOS.size()+"");
        redisService.set(HOT_PICTURE,pictureInfoDTOS,PICTURE_EXPIRE_TIME);

    }

    @Override
    public void setNewPictures() {
        List<Picture> pictures = pictureMapper.selectPictureByNew();
        Map<Integer,List<Tag>> lists = pictureTagService.getTagsByPicList(pictures);
        List<PictureInfoDTO> pictureInfoDTOS = new ArrayList<>();
        if(pictures==null||lists==null||pictureInfoDTOS==null){
            throw new CommonJsonException("无图片或图片信息为空");
        }

        for(int i=0;i<pictures.size();i++){
            PictureInfoDTO pictureInfoDTO = new PictureInfoDTO(pictures.get(i),lists.get(pictures.get(i).getPicId()));
            pictureInfoDTOS.add(pictureInfoDTO);
        }
        redisService.set(NEW_PICTURE,pictureInfoDTOS,PICTURE_EXPIRE_TIME);
    }
}

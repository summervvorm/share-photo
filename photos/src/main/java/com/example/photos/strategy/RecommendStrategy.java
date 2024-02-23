package com.example.photos.strategy;

import com.example.photos.model.dto.PictureInfoDTO;

import java.util.List;

/**
 * @Auther: raolongxiang
 * @Date: 2024/1/24
 * @Description: com.example.photos.strategy
 */
public interface RecommendStrategy {


    List<PictureInfoDTO> getRecommendPic(Integer userId);

    void filterRecommendPic(List<PictureInfoDTO>pictureInfoDTOS,Integer userId);

}

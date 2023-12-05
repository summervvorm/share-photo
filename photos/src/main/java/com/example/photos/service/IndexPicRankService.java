package com.example.photos.service;

import com.example.photos.model.dto.PictureInfoDTO;

import java.util.List;

/**
 * @Auther: Arrow
 * @Date: 2023/6/7
 * @Description: 首页的图片排行
 */
public interface IndexPicRankService {

    List<PictureInfoDTO> getHotPictures();

    List<PictureInfoDTO> getNewPictures();

    void setHotPictures();

    void setNewPictures();
}

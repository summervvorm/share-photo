package com.example.photos.service;

import com.example.photos.model.dto.PictureInfoDTO;

import java.util.List;

/**
 * @Auther: raolongxiang
 * @Date: 2023/12/6
 * @Description: com.example.photos.service
 */

public interface UserProfilesService {

    /**
    *@Description:根据用户偏好获取推荐图片列表
    */
    List<PictureInfoDTO> getPictures();

}

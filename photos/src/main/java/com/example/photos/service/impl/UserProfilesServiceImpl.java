package com.example.photos.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.photos.entity.UserProfiles;
import com.example.photos.mapper.UserProfilesMapper;
import com.example.photos.model.dto.PictureInfoDTO;
import com.example.photos.service.UserProfilesService;
import com.example.photos.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.photos.util.ListUtil.isNull;

/**
 * @Auther: raolongxiang
 * @Date: 2023/12/6
 * @Description: com.example.photos.service.impl
 */
@Service
@Slf4j
public class UserProfilesServiceImpl extends ServiceImpl<UserProfilesMapper, UserProfiles> implements UserProfilesService {


    @Override
    public List<PictureInfoDTO> getPictures() {
        Integer userId = UserUtil.getUserDetailsDTO().getUserId();

        if(userId==null){
            //Todo 游客推荐图片

        }else{
            //Todo 指定用户根据偏好推荐图片 先获取与权重标签id相同的图片和标签并且去掉用户最近浏览的图片，然后随机排序。
            List<PictureInfoDTO> res =  this.getPictures();
            return isNull(res)?null:res;

        }


        return null;
    }
}

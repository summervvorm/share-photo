package com.example.photos.service;

import com.example.photos.entity.LevelResource;
import com.example.photos.model.dto.PageResultDTO;
import com.example.photos.model.vo.ConditionVO;

import java.util.List;

/**
 * @Auther: Arrow
 * @Date: 2023/7/10
 * @Description: com.example.photos.service
 */
public interface LevelService {


    List<LevelResource> getAllLevelResource();


    PageResultDTO<LevelResource> getPageLevel(ConditionVO conditionVO);

    void updateLevel(LevelResource levelResource);

    void addLevel(LevelResource levelResource);

}

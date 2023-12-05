package com.example.photos.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.photos.entity.LevelResource;
import com.example.photos.mapper.LevelResourceMapper;
import com.example.photos.model.dto.PageResultDTO;
import com.example.photos.model.vo.ConditionVO;
import com.example.photos.service.LevelService;
import com.example.photos.util.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Auther: Arrow
 * @Date: 2023/7/10
 * @Description: com.example.photos.service.impl
 */
@Service
@Slf4j
public class LevelServiceImpl implements LevelService {

    @Resource
    LevelResourceMapper levelResourceMapper;

    @Override
    public List<LevelResource> getAllLevelResource() {

        return  levelResourceMapper.selectList(Wrappers.emptyWrapper());
    }

    @Override
    public PageResultDTO<LevelResource> getPageLevel(ConditionVO conditionVO) {

        Page<LevelResource> page =  new Page<>(PageUtil.getCurrent(), PageUtil.getSize());

        Page<LevelResource> res = levelResourceMapper.getPageLevelResource(page,conditionVO.getKeywords());

        return new PageResultDTO<>(res.getRecords(),(int)res.getTotal());
    }

    @Override
    public void updateLevel(LevelResource levelResource) {

        levelResourceMapper.update(levelResource,new LambdaUpdateWrapper<LevelResource>()
                .eq(LevelResource::getId,levelResource.getId())
        );

    }

    @Override
    public void addLevel(LevelResource levelResource) {

        levelResourceMapper.insert(levelResource);

    }
}

package com.example.photos.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.photos.entity.LevelResource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Auther: Arrow
 * @Date: 2023/7/10
 * @Description: com.example.photos.mapper
 */
@Repository
@Mapper
public interface LevelResourceMapper extends BaseMapper<LevelResource> {

    Page<LevelResource> getPageLevelResource(Page<LevelResource> page,@Param("keyword")String keywords);


}

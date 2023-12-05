package com.example.photos.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.photos.entity.FavoritesList;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Auther: Arrow
 * @Date: 2023/6/19
 * @Description: com.example.photos.mapper
 */
@Mapper
@Repository
public interface FavoritesListMapper extends BaseMapper<FavoritesList> {
}

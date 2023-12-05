package com.example.photos.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.photos.entity.Favorites;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Auther: Arrow
 * @Date: 2023/5/6
 * @Description: com.example.photos.mapper
 */
@Repository
@Mapper
public interface FavoritesMapper extends BaseMapper<Favorites> {

    List<Favorites>selectFavIdList(int userId);

    int insertFavList(List<Favorites> list);

    List<Integer>getFavPicIdByUserId(int userId);
}

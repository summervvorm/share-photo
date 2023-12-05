package com.example.photos.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.photos.entity.Picture;
import com.example.photos.model.dto.PictureStatsDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @Auther: Arrow
 * @Date: 2023/5/4
 * @Description: com.example.photos.mapper
 */
@Repository
@Mapper
public interface PictureMapper extends BaseMapper<Picture> {
    int selectIdByName(String name);
    int insertOne(Picture picture);
    List<Picture>selectPicturesByHot();
    List<Picture>selectPictureByNew();
    Map<String, BigDecimal> countLikesAndFavoritesByUserId(int userId);
    int updatePicLikes(@Param("picId") int picId, @Param("likesCount") int likesCount);
    int updatePicFav(@Param("picId") int picId, @Param("favCount") int favCount);
    int updatePicFavByDelFavList(@Param("userId")int userId,@Param("favListId") int favListId);
    Page<Picture> selectPicturesByKeywordWithSort(
            Page<Picture> page,@Param("keyword") String keyword,
            @Param("color") String color,
            @Param("orderByLikes") boolean orderByLikes,
            @Param("startTime")LocalDateTime startTime,
            @Param("endTime")LocalDateTime endTime,
            @Param("picWidth")Long picWidth,
            @Param("picHeight")Long picHeight,
            @Param("status")int status
            );
    List<PictureStatsDTO> getPicStats();

    Page<Picture> selectUploadPicturesByCondition(
            Page<Picture> page,@Param("keyword") String keyword,
            @Param("orderByLikes") boolean orderByLikes,
            @Param("status")int status,
            @Param("userId")int userId
    );


}

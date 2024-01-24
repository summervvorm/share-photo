package com.example.photos.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.photos.entity.UserImageActions;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Auther: raolongxiang
 * @Date: 2023/12/6
 * @Description: com.example.photos.mapper
 */
@Mapper
@Repository
public interface UserImageActionsMapper extends BaseMapper<UserImageActions> {

    @Select("SELECT p.pic_id,pic_name,pic_collections,pic_color,pic_by_id,pic_sign,pic_creat_time,`status`," +
            "width,size,height,pic_likes,t.tag_id,t.tag_name\n" +
            "FROM t_picture p\n" +
            "JOIN (SELECT subquery.pic_id, subquery.action_count\n" +
            "FROM (\n" +
            "    SELECT a.pic_id, COUNT(a.pic_id) AS action_count\n" +
            "    FROM t_user_image_actions a\n" +
            "    GROUP BY a.pic_id\n" +
            "    ORDER BY action_count DESC\n" +
            "    LIMIT 100)as subquery\n" +
            "ORDER BY RAND()\n" +
            "LIMIT 10) AS top_actions ON p.pic_id = top_actions.pic_id\n" +
            "LEFT JOIN t_picture_tag t ON p.pic_id = t.pic_id;\n")
    List<Map<String, Object>> getPopRecommendPic();


}

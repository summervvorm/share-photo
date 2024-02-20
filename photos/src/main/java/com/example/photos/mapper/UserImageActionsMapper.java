package com.example.photos.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.photos.entity.UserImageActions;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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


    @Select(" SELECT p.pic_id,pic_color,width,size,height,t.tag_name,top_actions.action_type,\n" +
            " top_actions.id as action_id\n" +
            " FROM t_picture p\n" +
            " JOIN (SELECT subquery.pic_id, subquery.action_count,subquery.action_type,subquery.id\n" +
            " FROM( \n" +
            " SELECT a.pic_id, COUNT(a.pic_id) AS action_count,a.action_type,a.id\n" +
            " FROM t_user_image_actions a\n" +
            " WHERE a.user_id = #{userId}\n" +
            " GROUP BY a.pic_id,a.action_type,a.id\n" +
            " ORDER BY action_count DESC\n" +
            " LIMIT 100)as subquery\n" +
            " ) AS top_actions ON p.pic_id = top_actions.pic_id\n" +
            " LEFT JOIN t_picture_tag t ON p.pic_id = t.pic_id\n" +
            " WHERE pic_by_id = #{userId}")
    List<Map<String, Object>> getUserFeaturePic(Integer userId);

    @Select("<script>" +
            "WITH picture_tags AS (\n" +
            "    SELECT p.pic_id, t.tag_name\n" +
            "    FROM `t_picture` p\n" +
            "    LEFT JOIN t_picture_tag t ON p.pic_id = t.pic_id\n" +
            ")\n" +
            "SELECT pt.pic_id, pt.tag_name\n" +
            "FROM picture_tags pt\n" +
            "JOIN (\n" +
            "    SELECT pic_id\n" +
            "    FROM picture_tags\n" +
            "    WHERE tag_name IN\n" +
            "    <foreach collection='list' item='item' index='index' separator=',' open='(' close=')'>\n" +
            "        #{item}\n" +
            "    </foreach>\n" +
            ") sub ON pt.pic_id = sub.pic_id;" +
            "</script>")
    List<Map<String, Object>> getPicFeatureByList(@Param("list") List<String> list);

    @Select("<script>" +
            "SELECT p.pic_id,p.pic_name,p.pic_likes,p.pic_collections,p.pic_sign,p.pic_color,p.pic_by_id,p.height," +
            "p.width,p.size,p.`status`,p.pic_creat_time,t.tag_name,t.tag_id " +
            "FROM t_picture_tag t LEFT JOIN t_picture p ON\n" +
            "t.pic_id = p.pic_id\n" +
            "WHERE p.pic_id in\n" +
            "    <foreach collection='list' item='item' index='index' separator=',' open='(' close=')'>\n" +
            "        #{item}\n" +
            "    </foreach> \n"+
            "</script>")
    List<Map<String,Object>>getPicInfoByList(List<Integer>list);


}

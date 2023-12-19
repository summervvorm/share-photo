package com.example.photos.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.photos.entity.Picture;
import com.example.photos.entity.UserProfiles;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
/**
 * @Auther: raolongxiang
 * @Date: 2023/12/6
 * @Description: com.example.photos.mapper
 */
@Mapper
@Repository
public interface UserProfilesMapper extends BaseMapper<UserProfiles> {
    @Select(
            "SELECT \n" +
                    "i.pic_id,i.pic_name,i.pic_likes,i.pic_collections,i.pic_color,i.pic_by_id,i.pic_sign,\n" +
                    "i.pic_creat_time,i.status,i.height,i.width,i.size\n"+
                    "    IFNULL(SUM(up.weight), 0) + (DATEDIFF(CURRENT_DATE, i.pic_creat_time) * ?) AS preference_score\n" +
                    "FROM t_picture AS i\n" +
                    "LEFT JOIN t_picture_tag AS it ON i.id = it.pic_id\n" +
                    "LEFT JOIN t_user_profiles AS up ON it.tag_id = up.tag_id AND up.user_id = ?\n" +
                    "LEFT JOIN t_user_image_actions AS uia ON i.id = uia.pic_id AND uia.user_id = ?\n" +
                    "WHERE (uia.pic_id IS NULL OR DATEDIFF(CURRENT_DATE, uia.create_time) > ?) -- 避免最近已经与之交互过的图片\n" +
                    "GROUP BY i.id, i.title, i.url\n" +
                    "ORDER BY preference_score DESC, RAND() -- 添加随机性并按偏好分数降序排列\n" +
                    "LIMIT 10; -- 限制结果数量，例如推荐前10张图片\n"
    )
    Picture getRecommendPicByUserId(Integer userId);


}

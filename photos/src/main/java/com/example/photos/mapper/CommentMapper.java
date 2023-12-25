package com.example.photos.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.photos.entity.Comment;
import com.example.photos.entity.Favorites;
import com.example.photos.model.dto.CommentDTO;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Auther: Arrow
 * @Date: 2023/6/13
 * @Description: com.example.photos.mapper
 */
@Mapper
@Repository
public interface CommentMapper extends BaseMapper<Comment> {

    @Select("SELECT id,c.user_id,pic_id,comment_content,reply_user_id,parent_id,type,status,c.create_time,c.update_time,\n" +
            "            user_nickname,user_header_image \n" +
            "            from t_comment c left join t_user u on c.user_id=u.user_id \n" +
            "where pic_id=#{picId}")
    List<CommentDTO> getCommentDTOByPicId(Integer picId);

}

package com.example.photos.service;

import com.example.photos.entity.Comment;
import com.example.photos.model.dto.CommentDTO;

import java.util.List;

/**
 * @Auther: Arrow
 * @Date: 2023/6/13
 * @Description: com.example.photos.service
 */
public interface CommentService {

    List<CommentDTO> getCommentDTOByPicId(Integer picId);

    Boolean insertComment(Comment comment);

}

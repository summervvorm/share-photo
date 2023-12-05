package com.example.photos.service;

import com.example.photos.entity.Comment;

import java.util.List;

/**
 * @Auther: Arrow
 * @Date: 2023/6/13
 * @Description: com.example.photos.service
 */
public interface CommentService {

    List<Comment> getCommentsByPicId(Integer picId);

    int insertComment(Comment comment);

}

package com.example.photos.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.photos.entity.Comment;
import com.example.photos.enums.StatusCodeEnum;
import com.example.photos.exception.CommonJsonException;
import com.example.photos.mapper.CommentMapper;
import com.example.photos.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: Arrow
 * @Date: 2023/6/13
 * @Description: com.example.photos.service.impl
 */
@Service
@Slf4j
public class CommentServiceImpl implements CommentService {
    @Autowired
    CommentMapper commentMapper;

    @Override
    public List<Comment> getCommentsByPicId(Integer picId) {
        List<Comment> comments=commentMapper.selectList(new LambdaQueryWrapper<Comment>()
                .eq(Comment::getPicId,picId).orderByDesc(Comment::getCreateTime)
        );
        if(comments==null){
            comments=new ArrayList<>();
        }
        return comments;
    }

    @Override
    public int insertComment(Comment comment) {
        comment.setCreateTime(LocalDateTime.now());
        int ans = commentMapper.insert(comment);
        if(ans==0){
            throw new CommonJsonException(StatusCodeEnum.FAIL);
        }
        return ans;
    }
}

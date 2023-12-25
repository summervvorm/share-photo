package com.example.photos.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.photos.entity.Comment;
import com.example.photos.enums.StatusCodeEnum;
import com.example.photos.exception.CommonJsonException;
import com.example.photos.mapper.CommentMapper;
import com.example.photos.model.dto.CommentDTO;
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
public class CommentServiceImpl extends ServiceImpl<CommentMapper,Comment> implements CommentService {

    @Override
    public List<CommentDTO> getCommentDTOByPicId(Integer picId) {
        List<CommentDTO> comments=this.baseMapper.getCommentDTOByPicId(picId);
        if(comments==null){
            comments=new ArrayList<>();
        }
        return comments;
    }

    @Override
    public Boolean insertComment(Comment comment) {
        boolean ans = this.save(comment);
        if(!ans){
            throw new CommonJsonException(StatusCodeEnum.FAIL);
        }
        return true;
    }
}

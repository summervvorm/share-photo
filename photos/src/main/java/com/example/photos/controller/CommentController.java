package com.example.photos.controller;

import com.example.photos.entity.Comment;
import com.example.photos.model.vo.ResultVO;
import com.example.photos.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @Auther: Arrow
 * @Date: 2023/6/13
 * @Description: com.example.photos.controller
 */
@RestController
@Slf4j
@Api(tags = "评论模块")
public class CommentController {
    @Autowired
    CommentService commentService;

    @ApiOperation("根据图片Id查询评论列表")
    @GetMapping("/comments/{picId}")
    public ResultVO<?> getCommentsByPicId(@Valid @PathVariable Integer picId){
        return ResultVO.ok(commentService.getCommentDTOByPicId(picId));
    }

    @ApiOperation("添加评论")
    @PostMapping("/comments")
    public ResultVO<?>addComment(@Valid@RequestBody Comment comment){
        commentService.insertComment(comment);
        return ResultVO.ok();
    }

}

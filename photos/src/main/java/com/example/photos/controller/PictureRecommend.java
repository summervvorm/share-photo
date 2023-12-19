package com.example.photos.controller;

import com.example.photos.model.vo.ResultVO;
import com.example.photos.model.vo.UserActionVO;
import com.example.photos.service.UserImageActionsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Auther: raolongxiang
 * @Date: 2023/12/6
 * @Description: com.example.photos.controller
 */
@Api("图片推荐模块")
@RequestMapping("/recommend")
@RestController
@Slf4j
public class PictureRecommend {


    @Resource
    UserImageActionsService userImageActionsService;

    @PostMapping("/actions")
    @ApiOperation("用户行为（点击，收藏，喜欢，上传，取消收藏等等。）")
    public ResultVO<?> actions(UserActionVO userActionVO){
        return ResultVO.ok(userImageActionsService.insertActions(userActionVO));
    }

    @GetMapping("/pictures")
    @ApiOperation("根据用户返回推荐图片(每次只返回10张图片)")
    public ResultVO<?> pictures(){


        return ResultVO.ok();
    }



}
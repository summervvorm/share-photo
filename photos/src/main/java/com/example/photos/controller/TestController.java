package com.example.photos.controller;

import com.example.photos.model.vo.NotificationVO;
import com.example.photos.model.vo.ResultVO;
import com.example.photos.util.WebSocketUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: Arrow
 * @Date: 2023/5/7
 * @Description: com.example.photos.controller
 */
@RestController
@Slf4j
@Api(tags = "测试模块")
public class TestController {

    @ApiOperation("发送消息")
    @GetMapping("/test/websocket")
    public ResultVO<?>websocket(String msg,String userId){
        WebSocketUtil.sendOneMessage(userId, NotificationVO.ok("message",msg));
        log.info(msg,userId);
        return ResultVO.ok();
    }

}

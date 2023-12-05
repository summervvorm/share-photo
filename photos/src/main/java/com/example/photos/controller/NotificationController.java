package com.example.photos.controller;

import com.example.photos.entity.Notification;
import com.example.photos.model.vo.ResultVO;
import com.example.photos.service.NotificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Auther: Arrow
 * @Date: 2023/7/5
 * @Description: com.example.photos.controller
 */
@RestController
@Slf4j
@Api(tags = "通知模块")
public class NotificationController {


    @Resource
    NotificationService notificationService;

    @ApiOperation("将消息设置为已读,并且获取信息列表")
    @PutMapping("/notifications/{userId}/{sender}")
    public ResultVO<?> settingNotifyState(@PathVariable int userId,@PathVariable String sender){
        log.info(userId+" "+sender);
        notificationService.updateNotifyState(userId,sender);

        return ResultVO.ok(notificationService.getNotificationByUserIdAndSender(userId,sender));
    }
    @ApiOperation("获取指定用户的消息列表")
    @GetMapping("/notifications/senderList/{userId}")
    public ResultVO<?>getNotificationSenderList(@PathVariable int userId){
        return ResultVO.ok( notificationService.getNotificationSenderList(userId));
    }

    @ApiOperation("发送消息给指定用户")
    @PostMapping("/notifications")
    public ResultVO<?>senderMsg(@RequestBody Notification notification){

        notificationService.senderMsg(notification);
        return ResultVO.ok();
    }


}

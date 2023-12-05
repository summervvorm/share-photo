package com.example.photos.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.photos.entity.Notification;
import com.example.photos.mapper.NotificationMapper;
import com.example.photos.model.vo.NotificationSenderListVO;
import com.example.photos.model.vo.NotificationVO;
import com.example.photos.service.NotificationService;
import com.example.photos.util.WebSocketUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

import static com.example.photos.constant.NotificationConstant.MESSAGE;

/**
 * @Auther: Arrow
 * @Date: 2023/7/5
 * @Description: com.example.photos.service.impl
 */

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    @Resource
    NotificationMapper notificationMapper;
    @Override
    public void updateNotifyState(int userId, String sender) {


        notificationMapper.update(null,new LambdaUpdateWrapper<Notification>()
                .eq(Notification::getReceiverId,userId)
                .eq(Notification::getSender,sender)
                .set(Notification::getState,"已读")
        );

    }

    @Override
    public List<NotificationSenderListVO> getNotificationSenderList(int userId) {

        return notificationMapper.getNotificationSenderList(userId);
    }

    @Override
    public List<Notification> getNotificationByUserIdAndSender(int userId, String sender) {

        return  notificationMapper.selectList(new LambdaQueryWrapper<Notification>()
                .eq(Notification::getReceiverId,userId)
                .eq(Notification::getSender,sender).orderByDesc(Notification::getCreateTime)
        );

    }

    @Override
    public void senderMsg(Notification notification) {
        notification.setCreateTime(LocalDateTime.now());

        WebSocketUtil.sendOneMessage(notification.getReceiverId()+"",NotificationVO.ok(MESSAGE,notification.getContent()));
        notificationMapper.insert(notification);

    }
}

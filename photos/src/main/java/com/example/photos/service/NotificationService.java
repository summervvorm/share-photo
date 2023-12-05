package com.example.photos.service;

import com.example.photos.entity.Notification;
import com.example.photos.model.vo.NotificationSenderListVO;

import java.util.List;

/**
 * @Auther: Arrow
 * @Date: 2023/7/5
 * @Description: com.example.photos.service
 */
public interface NotificationService {

    void updateNotifyState(int userId,String sender);

    List<NotificationSenderListVO>getNotificationSenderList(int userId);

    List<Notification>getNotificationByUserIdAndSender(int userId,String sender);

    void senderMsg(Notification notification);

}

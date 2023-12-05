package com.example.photos.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.photos.entity.Notification;
import com.example.photos.model.vo.NotificationSenderListVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Auther: Arrow
 * @Date: 2023/7/5
 * @Description: com.example.photos.mapper
 */
@Mapper
@Repository
public interface NotificationMapper extends BaseMapper<Notification> {

    List<NotificationSenderListVO>getNotificationSenderList(int userId);

    void insertList(List<Notification> notifications);

}

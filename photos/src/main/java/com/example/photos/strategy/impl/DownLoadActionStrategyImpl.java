package com.example.photos.strategy.impl;

import com.example.photos.model.event.UserActionEvent;
import com.example.photos.strategy.UserActionStrategy;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.photos.constant.RabbitMQConstant.REFRESH_USER_FEATURE_EXCHANGE;

/**
 * @Auther: raolongxiang
 * @Date: 2023/12/21
 * @Description: com.example.photos.strategy.impl
 */
@Service("downLoadActionStrategyImpl")
public class DownLoadActionStrategyImpl implements UserActionStrategy {

    @Autowired
    RabbitTemplate template;

    @Override
    public void execute(UserActionEvent userActionEvent) {


        template.convertAndSend(REFRESH_USER_FEATURE_EXCHANGE,"#",userActionEvent);


    }

}

package com.example.photos.consumer;

import com.alibaba.fastjson.JSON;
import com.example.photos.model.dto.EmailDTO;
import com.example.photos.util.EmailUtil;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.example.photos.constant.RabbitMQConstant.EMAIL_QUEUE;

/**
 * @Auther: Arrow
 * @Date: 2023/5/5
 * @Description: com.example.photos.consumer
 */
@Component
@RabbitListener(queues = EMAIL_QUEUE)
public class CommentNoticeConsumer {
    @Resource
    private EmailUtil emailUtil;

    @RabbitHandler
    public void process(byte[] data) {
        EmailDTO emailDTO = JSON.parseObject(new String(data), EmailDTO.class);
        emailUtil.sendHtmlMail(emailDTO);
    }
}

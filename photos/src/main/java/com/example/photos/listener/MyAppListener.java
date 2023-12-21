package com.example.photos.listener;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @Auther: raolongxiang
 * @Date: 2023/12/20
 * @Description: 测试springBoot项目启动有哪些事件触发。
 */
@Component
public class MyAppListener implements ApplicationListener<ApplicationReadyEvent> {

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        // 处理事件
        System.out.println("Event: " + event.getClass());
    }
}

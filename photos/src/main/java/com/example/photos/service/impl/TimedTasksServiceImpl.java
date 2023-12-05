package com.example.photos.service.impl;

import com.example.photos.service.TimedTasksService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Auther: Arrow
 * @Date: 2023/6/7
 * @Description: com.example.photos.service.impl
 */
@Service
@Slf4j
public class TimedTasksServiceImpl implements TimedTasksService {

    @Resource
    IndexPicRankServiceImpl indexPicRankService;

    @Scheduled(cron = "0 0 12 * * ?")
    public void setHotPictureTask(){
        log.info("这是定时任务 ");
        indexPicRankService.setHotPictures();
    }
    @Scheduled(cron = "0 0 12 * * ?")
    public void setNewPictureTask(){
        log.info("这是定时任务 ");
        indexPicRankService.setNewPictures();
    }

//    @Scheduled(cron="0 0/1 * * * ?")
    public void testTimeTask(){

    }

}

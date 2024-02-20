package com.example.photos.consumer;

import com.example.photos.mapper.UserImageActionsMapper;
import com.example.photos.model.event.UserActionEvent;
import com.example.photos.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static com.example.photos.constant.RabbitMQConstant.REFRESH_USER_FEATURE_QUEUE;

/**
 * @Auther: raolongxiang
 * @Date: 2024/2/1
 * @Description: com.example.photos.consumer
 */
@Component
@RabbitListener(queues = REFRESH_USER_FEATURE_QUEUE)
@Slf4j
public class RefreshUserFeatureConsumer {


    @Autowired
    RedisService redisService;

    @Autowired
    UserImageActionsMapper actionsMapper;

    @RabbitHandler
    public void handle(UserActionEvent userActionEvent) {

        // todo 用户画像，直接通过图片去获取标签，以此组成用户向量
        List<Map<String, Object>> list = actionsMapper.getUserFeaturePic(userActionEvent.getUserId());
        Map<String, Double> userFeature = new HashMap<>();

//        Set<String> set = new HashSet<>();
//
//        List<Map<String, Object>> picInfo = list.stream().filter(map -> {
//            String value = map.get("pic_id") + "_" + map.get("action_id");
//            if (!set.contains(value)) {
//                set.add(value);
//                return true;
//            }
//            return false;
//        }).collect(Collectors.toList());

        list.forEach(map -> {
            Double factor = getValueByActionType((Integer) map.getOrDefault("action_type", 0));
            userFeature.put((String) map.get("tag_name"), userFeature
                    .getOrDefault(map.get("tag_name"), 0.0) + factor);
        });
//        picInfo.forEach(map->{
//            Double factor = getValueByActionType((Integer) map.getOrDefault("action_type", 0));
//            userFeature.put((String) map.get("pic_color"), userFeature
//                    .getOrDefault(map.get("pic_color"), 0.0) + factor);
//
//            if((Integer)map.get("HighDefinition")*(Integer) map.get("height")>1080*1920){
//                userFeature.put("HighDefinition", userFeature
//                        .getOrDefault("", 0.0) + factor);
//            }
//        });
        log.info("更新用户画像....");
        redisService.set("userFeature:"+userActionEvent.getUserId(),userFeature);
    }

    private Double getValueByActionType(Integer t) {
//        @ApiModelProperty("0:点击.1:喜欢，2:取消喜欢，3:收藏,4:取消收藏,6:上传，7:下载")
        if (t == 0) {
            return 1.0;
        }
        if (t == 1 || t == 7) {
            return 1.5;
        }
        if (t == 3 || t == 6) {
            return 1.8;
        }
        return -0.5;
    }


}

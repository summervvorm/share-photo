package com.example.photos.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Auther: Arrow
 * @Date: 2023/7/4
 * @Description: com.example.photos.model.vo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationVO<T> {

    private String type;

    private T data;

    public static <T> NotificationVO<T> ok(String type,T data) {

        return notificationVO(type,data);

    }
    private static <T> NotificationVO<T> notificationVO(String type, T data) {
        return NotificationVO.<T>builder()
                .type(type)
                .data(data).build();
    }

    private static <T> NotificationVO<T>notificationVO(String type){
        return NotificationVO.<T>builder().type(type).build();
    }

}

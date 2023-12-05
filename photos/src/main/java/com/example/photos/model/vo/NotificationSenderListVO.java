package com.example.photos.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Auther: Arrow
 * @Date: 2023/7/5
 * @Description: com.example.photos.model.vo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationSenderListVO {
    private String name;
    private int unreadNumber;
}

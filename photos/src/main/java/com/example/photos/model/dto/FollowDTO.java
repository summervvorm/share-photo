package com.example.photos.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Auther: Arrow
 * @Date: 2023/6/20
 * @Description: com.example.photos.model.dto
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FollowDTO {
    private int userId;
    private int folId;
    private String folName;
    private String folAvatar;
    private String folSign;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)		// 反序列化
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createTime;
}

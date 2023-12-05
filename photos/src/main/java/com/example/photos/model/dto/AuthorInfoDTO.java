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
 * @Date: 2023/6/12
 * @Description: 图片作者信息
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthorInfoDTO {
    private int userId;

    private String nickname;

    private String header;

    private String sign;

    private int likesCount;

    private int favCount;

    private int fansCount;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)		// 反序列化
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createTime;
}

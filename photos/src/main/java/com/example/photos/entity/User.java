package com.example.photos.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.example.photos.service.impl.FavoritesServiceImpl;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * @Auther: Arrow
 * @Date: 2023-04-23
 * @Description: com.example.photos.domain
 * @version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_user")
public class User {
    @TableId(value = "user_id",type = IdType.AUTO)
    private int userId;
    private String userNickname;
    private String userPassword;
    private String userSign;
    private String userHeaderImage;
    @TableField(fill = FieldFill.INSERT)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)		// 反序列化
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createTime;
    private String userEmail;

    private int experience;
}

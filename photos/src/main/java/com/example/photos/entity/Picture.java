package com.example.photos.entity;

import com.baomidou.mybatisplus.annotation.*;
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
 * @Date: {MONTH} - {TIME}
 * @Description: com.example.photos.domain
 * @version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_picture")
public class Picture {
    @TableId(value = "pic_id",type = IdType.AUTO)
    private int picId;
    private String picName;
    private int picLikes;
    private int picCollections;
    @TableField("pic_color")
    private String picColor;
    private int picById;
    @TableField("pic_sign")
    private String picSign;
    @TableField(fill = FieldFill.INSERT)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)		// 反序列化
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime picCreatTime;
    @TableField("status")
    private int status;

    private int height;

    private int width;

    private Long size;
}

package com.example.photos.model.dto;

import com.example.photos.entity.Comment;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;

/**
 * @Auther: raolongxiang
 * @Date: 2023/12/25
 * @Description: com.example.photos.model.dto
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO{

    private Integer id;
    private Integer picId;
    private Integer userId;
    private String userName;
    private String userHeaderImage;
    private String commentContent;
    private Integer replyUserId;
    private Integer parentId;

    @ApiModelProperty("0：图片评论,1:个人留言")
    private Integer type;
    private Integer status;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)		// 反序列化
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createTime;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)		// 反序列化
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime updateTime;



}

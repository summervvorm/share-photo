package com.example.photos.model.dto;

import com.example.photos.entity.Picture;
import com.example.photos.entity.Tag;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Auther: Arrow
 * @Date: 2023/6/7
 * @Description: 用于展示图片的基本信息，如首页和搜索页面
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PictureInfoDTO {
    private int picId;
    private String picName;
    private int picLikes;
    private int picCollections;
    private String picSign;
    private List<Tag> tags;
    private String color;
    private int userId;
    private int picWidth;
    private int picHeight;
    private Long picSize;

    private int status;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)		// 反序列化
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime picCreatTime;

    public PictureInfoDTO(@NotNull Picture picture, List<Tag> tags){
        this.picId=picture.getPicId();
        this.picLikes=picture.getPicLikes();
        this.picCollections=picture.getPicCollections();
        this.userId=picture.getPicById();
        this.tags= tags;
        this.picSign=picture.getPicSign();
        this.color=picture.getPicColor();
        this.picCreatTime=picture.getPicCreatTime();
        this.picName=picture.getPicName();
        this.picSize=picture.getSize();
        this.picWidth=picture.getWidth();
        this.picHeight=picture.getHeight();
        this.status=picture.getStatus();
    }

}

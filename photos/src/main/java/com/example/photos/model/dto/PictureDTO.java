package com.example.photos.model.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Auther: Arrow
 * @Date: 2023/5/7
 * @Description: 单个图片的信息，不包含标签列表信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PictureDTO {
    private int picId;
    private String picName;
    private int picLikes;
    private int picCollections;
    private String picColor;
    private int picById;
    private String picSign;
    private LocalDateTime picCreatTime;
}

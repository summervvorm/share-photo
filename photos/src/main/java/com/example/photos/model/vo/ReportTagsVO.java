package com.example.photos.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Auther: Arrow
 * @Date: 2023/7/3
 * @Description: com.example.photos.model.vo
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportTagsVO {
    @ApiModelProperty(name = "userId",value = "用户id",dataType = "Integer")
    private int userId;
    @ApiModelProperty(name = "picId",value = "图片id",dataType = "Integer")
    private int picId;
    @ApiModelProperty(name = "tags",value = "图片标签",dataType = "List")
    private List<String>tags;
}

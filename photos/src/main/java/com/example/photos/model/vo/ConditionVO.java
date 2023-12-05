package com.example.photos.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @Auther: Arrow
 * @Date: 2023/5/7
 * @Description: 查询条件/包括收藏夹查询、图片查询。
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "查询条件")

public class ConditionVO {
    @ApiModelProperty(name = "current", value = "页码", dataType = "Long")
    private Long current;

    @ApiModelProperty(name = "size", value = "条数", dataType = "Long")
    private Long size;

    @ApiModelProperty(name = "keywords", value = "搜索内容", dataType = "String")
    private String keywords;

    @ApiModelProperty(name = "category", value = "分类标签", dataType = "String")
    private String category;

    @ApiModelProperty(name = "color", value = "颜色", dataType = "String")
    private String color;

    @ApiModelProperty(name = "picSize", value = "图片大小", dataType = "Long")
    private Long picSize;

    @ApiModelProperty(name = "picWidth", value = "图片宽度", dataType = "Long")
    private Long picWidth;

    @ApiModelProperty(name = "picHeight", value = "图片高度", dataType = "Long")
    private Long picHeight;

    @ApiModelProperty(name = "favListId", value = "收藏夹Id", dataType = "Integer")
    private int favListId;

    @ApiModelProperty(name = "startTime", value = "开始时间", dataType = "LocalDateTime")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startTime;

    @ApiModelProperty(name = "endTime", value = "结束时间", dataType = "LocalDateTime")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endTime;

    @ApiModelProperty(name = "isDelete", value = "是否删除", dataType = "Integer")
    private Integer isDelete;

    @ApiModelProperty(name = "isReview", value = "是否审核", dataType = "Integer")
    private Integer isReview;

    @ApiModelProperty(name = "status",value = "状态",dataType = "Integer")
    private Integer status;
}
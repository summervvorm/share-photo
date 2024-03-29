package com.example.photos.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @Auther: raolongxiang
 * @Date: 2023/12/6
 * @Description: com.example.photos.model.vo
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserActionVO {
    @NotNull(message = "用户ID不能为空")
    private int userId;

    @NotNull(message = "图片ID不能为空")
    private int picId;

    @NotNull(message = "行为类型不能为空")
    @ApiModelProperty("0:点击.1:喜欢，2:取消喜欢，3:收藏,4:取消收藏,6:上传，7:下载")
    private int actionType;
}

package com.example.photos.model.event;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Auther: raolongxiang
 * @Date: 2024/1/24
 * @Description: com.example.photos.model.event
 */
@Data
public class UserActionEvent {

    private Integer userId;
    private List<Integer> picId;

    @ApiModelProperty("0:点击.1:喜欢，2:取消喜欢，3:收藏,4:取消收藏,6:上传，7:下载")
    private Integer actionType;

}

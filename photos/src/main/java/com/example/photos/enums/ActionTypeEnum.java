package com.example.photos.enums;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @Auther: raolongxiang
 * @Date: 2023/12/20
 * @Description: com.example.photos.enums
 */
@AllArgsConstructor
@Getter
public enum ActionTypeEnum {

//    @ApiModelProperty("0:点击.1:喜欢，2:取消喜欢，3:收藏,4:取消收藏,6:上传，7:下载")

    CLICK(0, ""),
    Like(1, "likeActionStrategyImpl"),
    FAVORITES(3, "favoritesActionStrategyImpl"),
    VIEW(4, "viewActionStrategyImpl"),
    DOWNLOAD(7, "downLoadActionStrategyImpl");
    private final Integer actionType;

    private final String strategy;

    public static String getStrategy(Integer actionType) {
        for (ActionTypeEnum value : ActionTypeEnum.values()) {
            if (Objects.equals(value.getActionType(), actionType)) {
                return value.getStrategy();
            }
        }
        return null;
    }


}

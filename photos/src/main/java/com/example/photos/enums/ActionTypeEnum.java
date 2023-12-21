package com.example.photos.enums;
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

    CLICK(0,""),
    Like(1,""),
    FAVORITES(2,""),
    DOWNLOAD(3,""),
    VIEW(4,"viewActionStrategyImpl");

    private final Integer actionType;

    private final String strategy;

    public static String getStrategy(Integer actionType){
        for (ActionTypeEnum value : ActionTypeEnum.values()) {
            if (Objects.equals(value.getActionType(), actionType)) {
                return value.getStrategy();
            }
        }
        return null;
    }


}

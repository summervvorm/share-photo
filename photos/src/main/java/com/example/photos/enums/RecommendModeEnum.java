package com.example.photos.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @Auther: raolongxiang
 * @Date: 2024/1/24
 * @Description: com.example.photos.enums
 */
@AllArgsConstructor
@Getter
public enum RecommendModeEnum {

    POP("pop", "popRecommendStrategyImpl"),
    CONTENT_BASE("contentBase","contentBaseStrategyImpl");
    private final String mode;

    private final String strategy;

    public static String getStrategy(String mode){
        for (RecommendModeEnum value : RecommendModeEnum.values()) {
            if (Objects.equals(value.getMode(), mode)) {
                return value.getStrategy();
            }
        }
        return null;
    }
}

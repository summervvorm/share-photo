package com.example.photos.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Auther: Arrow
 * @Date: 2023/4/26
 * @Description: com.example.photos.enums
 */
@AllArgsConstructor
@Getter
public enum UploadModeEnum {
    OSS("oss", "ossUploadStrategyImpl");

    private final String mode;

    private final String strategy;

    public static String getStrategy(String mode){
        for (UploadModeEnum value : UploadModeEnum.values()) {
            if (value.getMode().equals(mode)) {
                return value.getStrategy();
            }
        }
        return null;
    }

}

package com.example.photos.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Auther: Arrow
 * @Date: 2023/4/26
 * @Description: com.example.photos.enums
 */
@Getter
@AllArgsConstructor
public enum FilePathEnum {
    AVATAR("photos/header/", "头像路径"),

//    ARTICLE("aurora/articles/", "文章图片路径"),

//    VOICE("aurora/voice/", "音频路径"),

    PHOTO("photos/photos/", "相册路径"),

    ADVERTISEMENTS("photos/advertisements","广告图片路径");

//    CONFIG("aurora/config/", "配置图片路径"),

//    TALK("aurora/talks/", "配置图片路径"),

//    MD("aurora/markdown/", "md文件路径");

    private final String path;

    private final String desc;

}

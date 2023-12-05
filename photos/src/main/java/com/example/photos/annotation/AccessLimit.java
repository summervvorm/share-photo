package com.example.photos.annotation;

import java.lang.annotation.*;

/**
 * @Auther: Arrow
 * @Date: 2023/5/5
 * @Description: com.example.photos.annotation
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AccessLimit {
    int seconds();

    int maxCount();
}

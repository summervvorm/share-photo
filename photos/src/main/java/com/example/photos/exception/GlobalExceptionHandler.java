package com.example.photos.exception;

import com.alibaba.fastjson.JSONObject;
import com.example.photos.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @Auther: Arrow
 * @Date: 2023/4/24
 * @Description: com.example.photos.exception
 */

@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 本系统自定义错误的拦截器
     * 拦截到此错误之后,就返回这个类里面的json给前端
     * 常见使用场景是参数校验失败,抛出此错,返回错误信息给前端
     */
    @ExceptionHandler(CommonJsonException.class)
    public JSONObject commonJsonExceptionHandler(CommonJsonException commonJsonException) {
        return commonJsonException.getResultJson();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public JSONObject commonJsonExceptionHandler(MethodArgumentNotValidException methodArgumentNotValidException) {
        log.error("[ @Vaild异常捕获 ] "+ methodArgumentNotValidException.getMessage());

        return CommonUtil.errorJson(52000,methodArgumentNotValidException.getFieldError().getDefaultMessage());
    }
}

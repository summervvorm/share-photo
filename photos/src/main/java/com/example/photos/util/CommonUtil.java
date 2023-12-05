package com.example.photos.util;

import com.alibaba.fastjson.JSONObject;
import com.example.photos.enums.StatusCodeEnum;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Auther: Arrow
 * @Date: 2023/4/24
 * @Description: com.example.photos.util
 */
@Slf4j
public class CommonUtil {


    public static JSONObject errorJson(StatusCodeEnum errorEnum) {
        JSONObject resultJson = new JSONObject();
        resultJson.put("code", errorEnum.getCode());
        resultJson.put("message", errorEnum.getDesc());
//        resultJson.put("info", new JSONObject());
        return resultJson;
    }

    public static JSONObject errorJson(String msg) {
        JSONObject resultJson = new JSONObject();
        resultJson.put("message", msg);
        return resultJson;
    }

    public static JSONObject errorJson(int code, String msg) {
        JSONObject resultJson = new JSONObject();
        resultJson.put("code", code);
        resultJson.put("message", msg);
        return resultJson;
    }

    public static String getRandomCode() {
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            str.append(random.nextInt(10));
        }
        return str.toString();
    }

    public static LocalDateTime getLocalDateTime() {
        LocalDateTime date = LocalDateTime.now();
        return date;
    }

    public static boolean checkEmail(String username) {
        String rule = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
        //正则表达式的模式 编译正则表达式
        Pattern p = Pattern.compile(rule);
        //正则表达式的匹配器
        Matcher m = p.matcher(username);
        //进行正则匹配
        return m.matches();
    }


}

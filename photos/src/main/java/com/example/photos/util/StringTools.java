package com.example.photos.util;

/**
 * @Auther: Arrow
 * @Date: 2023/4/24
 * @Description: com.example.photos.util
 */
public class StringTools {
    public static boolean isNullOrEmpty(String str) {
        return null == str || "".equals(str) || "null".equals(str);
    }

    public static boolean isNullOrEmpty(Object obj) {
        return null == obj || "".equals(obj);
    }


    public static boolean isConnection(String str1,String str2){

        return str1.contains(str2)||str2.contains(str1);

//        return false;
    }


}

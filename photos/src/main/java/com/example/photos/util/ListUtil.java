package com.example.photos.util;

import java.util.List;

/**
 * @Auther: raolongxiang
 * @Date: 2023/12/19
 * @Description: com.example.photos.util
 */
public class ListUtil {


    public static boolean isNull(List<?>list){
        return list==null;
    }
    public static boolean isNullOrEmpty(List<?>list){
        return list==null||list.size()==0;
    }
}

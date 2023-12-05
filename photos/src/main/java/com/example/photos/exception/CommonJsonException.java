package com.example.photos.exception;

import com.alibaba.fastjson.JSONObject;
import com.example.photos.enums.StatusCodeEnum;
import com.example.photos.util.CommonUtil;
import lombok.Builder;


/**
 * @Auther: Arrow
 * @Date: 04 - 21:25
 * @Description: com.example.photos.exception
 */
@Builder
public class CommonJsonException extends RuntimeException{
    private final JSONObject resultJson;
    /**
     * 调用时可以在任何代码处直接throws这个Exception,
     * 都会统一被拦截,并封装好json返回给前台
     *
     * @param errorEnum 以错误的ErrorEnum做参数
     */

    public CommonJsonException(StatusCodeEnum errorEnum) {
        this.resultJson = CommonUtil.errorJson(errorEnum);;
    }
    public CommonJsonException(String msg) {
        this.resultJson = CommonUtil.errorJson(msg);
    }

    public CommonJsonException(JSONObject resultJson) {
        this.resultJson = resultJson;
    }


    public JSONObject getResultJson() {
        return resultJson;
    }
}

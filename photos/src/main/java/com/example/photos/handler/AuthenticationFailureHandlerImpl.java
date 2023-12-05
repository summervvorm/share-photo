package com.example.photos.handler;

import com.alibaba.fastjson.JSON;
import com.example.photos.constant.CommonConstant;
import com.example.photos.model.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Auther: Arrow
 * @Date: 2023/5/8
 * @Description: com.example.photos.handler
 */
@Component
@Slf4j
public class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException{
        String message=exception.getMessage();
        log.info("验证失败："+message);
        response.setContentType(CommonConstant.APPLICATION_JSON);
        response.getWriter().write(JSON.toJSONString(ResultVO.fail(message)));
    }
}

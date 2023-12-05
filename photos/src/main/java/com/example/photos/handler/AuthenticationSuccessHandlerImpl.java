package com.example.photos.handler;

import com.alibaba.fastjson.JSON;
import com.example.photos.constant.CommonConstant;
import com.example.photos.model.dto.UserDetailsDTO;
import com.example.photos.model.vo.ResultVO;
import com.example.photos.service.TokenService;
import com.example.photos.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @Auther: Arrow
 * @Date: 2023/5/8
 * @Description: com.example.photos.handler
 */
@Component
@Slf4j
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {

    @Autowired
    private TokenService tokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserDetailsDTO userDetailsDTO = UserUtil.getUserDetailsDTO();
        String token="";
        if (Objects.nonNull(authentication)) {
            token = tokenService.createToken(userDetailsDTO);
            log.info("这是新的token:  "+token);
//            log.info("这是UserDetail: "+userDetailsDTO.toString());
        }
        response.setContentType(CommonConstant.APPLICATION_JSON);
        response.getWriter().write(JSON.toJSONString(ResultVO.ok(token)));

    }



}

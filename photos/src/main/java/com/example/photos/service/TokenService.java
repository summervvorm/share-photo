package com.example.photos.service;

import javax.servlet.http.HttpServletRequest;

import com.example.photos.model.dto.UserDetailsDTO;
import io.jsonwebtoken.Claims;

/**
 * @Auther: Arrow
 * @Date: 2023/5/7
 * @Description: com.example.photos.service
 */
public interface TokenService {
    String createToken(UserDetailsDTO userDetailsDTO);

    String createToken(String subject);

    void refreshToken(UserDetailsDTO userDetailsDTO);

    void renewToken(UserDetailsDTO userDetailsDTO);

    Claims parseToken(String token);

    UserDetailsDTO getUserDetailDTO(HttpServletRequest request);

    void delLoginUser(String username);

}

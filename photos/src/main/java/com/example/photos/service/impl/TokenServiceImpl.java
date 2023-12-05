package com.example.photos.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.photos.exception.CommonJsonException;
import com.example.photos.model.dto.UserDetailsDTO;
import com.example.photos.service.RedisService;
import com.example.photos.service.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

import static com.example.photos.constant.AuthConstant.*;
import static com.example.photos.constant.RedisConstant.LOGIN_USER;

/**
 * @Auther: Arrow
 * @Date: 2023/5/7
 * @Description: com.example.photos.service.impl
 */
@Service
@Slf4j
public class TokenServiceImpl implements TokenService {
    @Autowired
    private RedisService redisService;



    @Override
    public String createToken(UserDetailsDTO userDetailsDTO) {
        refreshToken(userDetailsDTO);
        String userId = userDetailsDTO.getUsername();
        return createToken(userId);
    }

    @Override
    public String createToken(String subject) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        SecretKey secretKey = generalKey();
        return Jwts.builder().setId(getUuid()).setSubject(subject).setIssuer("xiachong").signWith(signatureAlgorithm,secretKey).compact();
    }

    private String getUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    private SecretKey generalKey() {
        byte[] encodedKey = Base64.getDecoder().decode(SECRET);
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }

    @Override
    public void refreshToken(UserDetailsDTO userDetailsDTO) {
        LocalDateTime currentTime = LocalDateTime.now();
        userDetailsDTO.setExpireTime(currentTime.plusSeconds(EXPIRE_TIME));
        int userId = userDetailsDTO.getUserId();
        String UserDetailDTOJSON= JSON.toJSONString(userDetailsDTO);
        redisService.hSet(LOGIN_USER, userDetailsDTO.getUsername(), UserDetailDTOJSON, EXPIRE_TIME);
    }

    @Override
    public void renewToken(UserDetailsDTO userDetailsDTO) {
        LocalDateTime expireTime = userDetailsDTO.getExpireTime();
        LocalDateTime currentTime = LocalDateTime.now();
        if (Duration.between(currentTime, expireTime).toMinutes() <= TWENTY_MINUTES) {
            refreshToken(userDetailsDTO);
        }
    }

    @Override
    public Claims parseToken(String token) {
        SecretKey secretKey = generalKey();
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    @Override
    public UserDetailsDTO getUserDetailDTO(HttpServletRequest request) {
        String token = Optional.ofNullable(request.getHeader(TOKEN_HEADER)).orElse("");
        if(StringUtils.hasText(token)&&!token.equals("null")){
            Claims claims = parseToken(token);
            String username = claims.getSubject();
            Object o = redisService.hGet(LOGIN_USER, username);
            UserDetailsDTO userDetailsDTO = new UserDetailsDTO();
//            log.info("这是userDetail:"+o.toString());
            if(o==null){
                return null;
            }
            if(o.getClass().isAssignableFrom(String.class)){


                userDetailsDTO= JSON.parseObject(o.toString(),UserDetailsDTO.class);
            }else{
                userDetailsDTO=(UserDetailsDTO) o;
            }
//            log.info(userDetailsDTO.toString());
            return userDetailsDTO;
        }

        return null;
    }

    @Override
    public void delLoginUser(String username) {
        redisService.hDel(LOGIN_USER, username);
    }
}

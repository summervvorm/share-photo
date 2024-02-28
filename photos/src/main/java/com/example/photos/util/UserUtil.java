package com.example.photos.util;

import com.example.photos.exception.CommonJsonException;
import com.example.photos.model.dto.UserDetailsDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static com.example.photos.enums.StatusCodeEnum.NO_LOGIN;

/**
 * @Auther: Arrow
 * @Date: 2023/5/7
 * @Description: com.example.photos.util
 */
@Component
@Slf4j
public class UserUtil {
    public static UserDetailsDTO getUserDetailsDTO() {
//        log.info("这是UserDetail: "+);
        if(SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")){
            log.info("返回的是一个空UserDetailDTO");

            throw new CommonJsonException(NO_LOGIN);
//            return new UserDetailsDTO();
        }

        return (UserDetailsDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}

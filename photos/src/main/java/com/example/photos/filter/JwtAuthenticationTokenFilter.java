package com.example.photos.filter;

import com.example.photos.model.dto.UserDetailsDTO;
import com.example.photos.service.TokenService;
import com.example.photos.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

import static com.example.photos.constant.AuthConstant.TOKEN_HEADER;

/**
 * @Auther: Arrow
 * @Date: 2023/5/7
 * @Description: com.example.photos.filter
 */
@Component
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Autowired
    TokenService tokenService;

    @Autowired
    AuthenticationEntryPoint authenticationEntryPoint;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        UserDetailsDTO userDetailsDTO = tokenService.getUserDetailDTO(request);
//        log.info("过滤：  "+request.getHeader(TOKEN_HEADER));
        if (Objects.nonNull(userDetailsDTO) && Objects.isNull(UserUtil.getAuthentication())) {
            log.info("请求过滤：  "+userDetailsDTO);
            tokenService.renewToken(userDetailsDTO);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetailsDTO, null, userDetailsDTO.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request, response);
    }
}

package com.example.photos.interceptor;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.photos.util.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Objects;
import java.util.Optional;

import static com.example.photos.constant.CommonConstant.*;

/**
 * @Auther: Arrow
 * @Date: 2023/5/7
 * @Description: com.example.photos.interceptor
 */
@Component
@SuppressWarnings("all")
@Slf4j
public class PaginationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String currentPage = request.getParameter(CURRENT);//当前多少页
        String pageSize = Optional.ofNullable(request.getParameter(SIZE)).orElse(DEFAULT_SIZE);//一页的大小
        if (!Objects.isNull(currentPage) && !StringUtils.isEmpty(currentPage)) {
            PageUtil.setCurrentPage(new Page<>(Long.parseLong(currentPage), Long.parseLong(pageSize)));
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        PageUtil.remove();
    }
}

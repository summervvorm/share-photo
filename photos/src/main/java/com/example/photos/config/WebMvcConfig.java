package com.example.photos.config;

import com.example.photos.interceptor.AccessLimitInterceptor;
import com.example.photos.interceptor.PaginationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @Auther: Arrow
 * @Date: 2023/5/10
 * @Description: com.example.photos.config
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private PaginationInterceptor paginationInterceptor;

    @Resource
    private AccessLimitInterceptor accessLimitInterceptor;
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedHeaders("*")
                .allowedOriginPatterns("*")
                .exposedHeaders("*")
                .allowedMethods(new String[]{"GET", "POST", "PUT", "DELETE"})
        ;
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(paginationInterceptor).addPathPatterns("/**");
        registry.addInterceptor(accessLimitInterceptor).addPathPatterns("/**");

    }


}

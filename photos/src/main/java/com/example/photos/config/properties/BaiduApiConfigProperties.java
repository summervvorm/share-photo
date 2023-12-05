package com.example.photos.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Auther: Arrow
 * @Date: 2023/7/17
 * @Description: com.example.photos.config.properties
 */
@ConfigurationProperties(prefix = "baiduapi")
@Data
@Component
public class BaiduApiConfigProperties {

    private String url;
    private String accessToken;
    private String apikey;
    private String secretKey;
    private String authHost;

}

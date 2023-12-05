package com.example.photos.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther: Arrow
 * @Date: 2023/4/26
 * @Description: com.example.photos.config.properties
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "upload.oss")
public class OssConfigProperties {

    private String url;

    private String endpoint;

    private String accessKeyId;

    private String accessKeySecret;

    private String bucketName;

}

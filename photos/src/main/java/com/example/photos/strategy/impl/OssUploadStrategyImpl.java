package com.example.photos.strategy.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import com.example.photos.config.properties.OssConfigProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;/**
 * @Auther: Arrow
 * @Date: 2023/4/26
 * @Description: com.example.photos.strategy.impl
 */
@Service("ossUploadStrategyImpl")
@Slf4j
public class OssUploadStrategyImpl extends AbstractUploadStrategyImpl{

    @Resource
    private OssConfigProperties ossConfigProperties;

    @Override
    public Boolean exists(String filePath) {
//        log.error(getOssClient().listBuckets().toString()+"asdasda");
        return getOssClient().doesObjectExist(ossConfigProperties.getBucketName(),filePath);
    }

    @Override
    public void upload(String path, String fileName, InputStream inputStream) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType("image/jpeg");
        getOssClient().putObject(ossConfigProperties.getBucketName(),path+fileName,inputStream,objectMetadata);
        Date expiration = new Date(System.currentTimeMillis() + 3600 * 1000);
        String url = getOssClient().generatePresignedUrl(ossConfigProperties.getBucketName(), path+fileName, expiration).toString();
    }

    @Override
    public String getFileAccessUrl(String filePath) {
        return ossConfigProperties.getUrl()+filePath;
    }
    private OSS getOssClient() {
        return new OSSClientBuilder().build(ossConfigProperties.getEndpoint(), ossConfigProperties.getAccessKeyId(), ossConfigProperties.getAccessKeySecret());
    }


    @Override
    public void deleteFile(String fileName) {


        getOssClient().deleteObject(ossConfigProperties.getBucketName(),fileName);
    }
}

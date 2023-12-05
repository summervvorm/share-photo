package com.example.photos.strategy;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * @Auther: Arrow
 * @Date: 2023/4/26
 * @Description: com.example.photos.strategy
 */
public interface UploadStrategy {
    String uploadFile(MultipartFile file, String path);

    String uploadFile(String fileName, InputStream inputStream, String path);

    void deleteFiles(String fileName);
}

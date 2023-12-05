package com.example.photos.strategy.impl;

import com.example.photos.exception.CommonJsonException;
import com.example.photos.strategy.UploadStrategy;
import com.example.photos.util.FileUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

import static com.example.photos.util.UserUtil.getUserDetailsDTO;

/**
 * @Auther: Arrow
 * @Date: 2023/4/26
 * @Description: com.example.photos.strategy.impl
 */
@Service
public abstract class AbstractUploadStrategyImpl implements UploadStrategy {

    @Override
    public String uploadFile(MultipartFile file, String path) {
        try {
            String md5 = FileUtil.getMd5(file.getInputStream());
            String extName = FileUtil.getExtName(file.getOriginalFilename());
            String userId = getUserDetailsDTO().getUserId()+"";
            String fileName = md5 +userId+extName;
            if (!exists(path + fileName)) {
                upload(path, fileName, file.getInputStream());
            }
            return getFileAccessUrl(path + fileName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommonJsonException("文件上传失败");
        }
    }

    @Override
    public String uploadFile(String fileName, InputStream inputStream, String path) {
        try {
            upload(path, fileName, inputStream);
            return getFileAccessUrl(path + fileName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommonJsonException("文件上传失败");
        }
    }

    public void deleteFiles(String fileName){
        try {
            if(exists(fileName))
                deleteFile(fileName);
            else
                throw new CommonJsonException("文件不存在");
        }catch (Exception e){
            throw new CommonJsonException("文件删除失败");
        }
    }

    public abstract void deleteFile(String fileName);

    public abstract Boolean exists(String filePath);

    public abstract void upload(String path, String fileName, InputStream inputStream) throws IOException;

    public abstract String getFileAccessUrl(String filePath);
}

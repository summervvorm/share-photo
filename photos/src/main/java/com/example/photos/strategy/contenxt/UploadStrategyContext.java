package com.example.photos.strategy.contenxt;

import com.example.photos.strategy.UploadStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Map;

import static com.example.photos.enums.UploadModeEnum.getStrategy;

/**
 * @Auther: Arrow
 * @Date: 2023/4/26
 * @Description: com.example.photos.strategy.contenxt
 */
@Service
@Slf4j
public class UploadStrategyContext {

    @Value("${upload.mode}")
    private String uploadMode;

    @Autowired
    private Map<String, UploadStrategy> uploadStrategyMap;


    /**
     *
     * @param: file
     * @param: path
     * @return 上传文件
     */
    public String executeUploadStrategy(MultipartFile file, String path) {
        return uploadStrategyMap.get(getStrategy(uploadMode)).uploadFile(file, path);
    }

    public String executeUploadStrategy(String fileName, InputStream inputStream, String path) {
        return uploadStrategyMap.get(getStrategy(uploadMode)).uploadFile(fileName, inputStream, path);
    }

    /**
    功能描述:删除文件
    @param: fileName
    @return: void
    @auther: Arrow
    @Date: 2023/5/15
    */
    public void executeDeleteStrategy(String fileName){
        String s="http://arrows.oss-cn-beijing.aliyuncs.com/";
        if(fileName.contains("http://arrows.oss-cn-beijing.aliyuncs.com/")){
            fileName=fileName.substring(s.length());
            log.info(fileName);
        }
        uploadStrategyMap.get(getStrategy(uploadMode)).deleteFiles(fileName);
    }


}

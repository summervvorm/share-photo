package com.example.photos.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Auther: Arrow
 * @Date: 2023/5/11
 * @Description: com.example.photos.model.vo
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PictureUploadVO {
    @ApiModelProperty(name = "picSign",value = "图片描述",dataType = "String")
    @NotNull(message = "图片描述不能为空")
    private String picSign;

    @ApiModelProperty(name = "picClass",value = "图片标签",dataType = "List")
    @NotNull(message = "标签不能为空")
    private List<String> picTags;

    @ApiModelProperty(name = "file",value = "图片文件",dataType = "MultipartFile")
    @NotNull(message = "图片文件不能为空")
    private MultipartFile file;
}

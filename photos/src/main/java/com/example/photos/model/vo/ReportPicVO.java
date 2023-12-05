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
 * @Date: 2023/7/6
 * @Description: com.example.photos.model.vo
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportPicVO {

    @ApiModelProperty(name = "userId",value = "用户id",dataType = "Integer")
    @NotNull(message = "用户ID不能为空")
    private int userId;

    @ApiModelProperty(name = "picId",value = "图片id",dataType = "Integer")
    @NotNull(message = "图片ID不能为空")
    private int picId;

    @ApiModelProperty(name = "category",value = "分类",dataType = "String")
    @NotNull(message = "分类不能为空")
    private String category;

    @ApiModelProperty(name = "content",value = "内容",dataType = "String")
    @NotNull(message = "描述内容不能为空")
    private String content;

}

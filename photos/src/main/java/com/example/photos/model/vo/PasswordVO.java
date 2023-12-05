package com.example.photos.model.vo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Auther: Arrow
 * @Date: 2023/6/28
 * @Description: com.example.photos.model.vo
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordVO {
    @ApiModelProperty(name = "oldPassword",value = "旧密码",dataType = "String")
    private String oldPassword;
    @ApiModelProperty(name = "newPassword",value = "新密码",dataType = "String")
    private String newPassword;
}

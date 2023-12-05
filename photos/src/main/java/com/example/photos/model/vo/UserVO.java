package com.example.photos.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @Auther: Arrow
 * @Date: 2023/4/24
 * @Description: com.example.photos.model.vo
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserVO {
    @NotBlank(message = "邮箱不能为空")
    private String username;


    @NotBlank(message = "昵称不能为空")
    private String nickname;

    @Size(min =3,message = "密码不能少于3位")
    @Size(max = 15,message = "密码不能超过15位")
    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "验证码不能为空")
    private String emailCode;

}

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
 * @Date: 2023/4/25
 * @Description: 用户信息对象
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoVO {
    @NotNull(message = "ID不能为空")
    private int userId;

    @NotBlank(message = "昵称不能为空")
//    @Size(max = 8,message = "昵称不能超过8个字")
    private String nickname;


    @Size(max = 20,message = "签名不能超过20个字")
    private String sign;

}

package com.example.photos.model.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * @Auther: Arrow
 * @Date: 2023/4/26
 * @Description: com.example.photos.model.dto
 */

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private int UserId;

    private String password;

    private String nickname;

    private String header;

    private String sign;

    private LocalDateTime lastLoginTime;

    private String userEmail;

    private String token;
}

package com.example.photos.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Auther: Arrow
 * @Date: 2023/5/7
 * @Description: com.example.photos.entity
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_role_resource")
public class RoleResource {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer roleId;

    private Integer resourceId;
}

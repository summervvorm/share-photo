package com.example.photos.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Auther: Arrow
 * @Date: 2023/6/14
 * @Description: com.example.photos.model.vo
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FollowVO {
    @ApiModelProperty(name = "userId",value = "用户ID",dataType = "Integer")
    private int userId;
    @ApiModelProperty(name = "userName",value = "用户昵称",dataType = "String")
    private String userName;
    @ApiModelProperty(name = "fansId",value = "粉丝ID",dataType = "Integer")
    private int fansId;
    @ApiModelProperty(name = "fansName",value = "粉丝昵称",dataType = "String")
    private String fansName;
    @ApiModelProperty(name = "toFollow",value = "是否关注",dataType = "boolean")
    private boolean toFollow;
}

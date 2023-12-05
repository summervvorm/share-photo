package com.example.photos.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Auther: Arrow
 * @Date: {MONTH} - {TIME}
 * @Description: com.example.photos.domain
 * @version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_follow")
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.WRAPPER_ARRAY)
public class Follow {
    @TableId(value = "fol_id", type = IdType.AUTO)
    private int folId;
    private int folUserId;
    private int folFansId;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime CreateTime;
}

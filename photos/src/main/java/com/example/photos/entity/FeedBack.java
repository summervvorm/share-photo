package com.example.photos.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Auther: Arrow
 * @Date: 2023/5/15
 * @Description: com.example.photos.entity
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_feedback")
public class FeedBack {
    @TableId(value = "fav_id", type = IdType.AUTO)
    private int feeId;
    private String isSolved;
    private int byUserId;
    private int byPicId;
    private String feeInfo;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime CreateTime;
}

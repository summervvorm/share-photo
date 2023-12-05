package com.example.photos.entity;

import com.baomidou.mybatisplus.annotation.*;
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
@TableName("upload")
public class Upload {
    @TableId(value = "upl_id",type = IdType.AUTO)
    private int uplId;
    private String uplUserId;
    private String uplPicId;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime uplCreatTime;
}

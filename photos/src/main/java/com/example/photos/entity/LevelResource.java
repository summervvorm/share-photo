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
 * @Date: 2023/7/10
 * @Description: com.example.photos.entity
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_level_resource")
public class LevelResource {
    private int id;
    private String levelName;
    private int downloadCount;
    @TableId(value = "requires_experience")
    private int requiresExperience;
    private int IsDisplayAd;
}

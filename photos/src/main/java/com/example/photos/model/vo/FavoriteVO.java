package com.example.photos.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @Auther: Arrow
 * @Date: 2023/5/6
 * @Description: com.example.photos.model.vo
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteVO {

    @NotNull(message = "图片ID不能为空")
    private int picId;

    @NotNull(message = "收藏夹名不能为空")
    private String CollectName;

}

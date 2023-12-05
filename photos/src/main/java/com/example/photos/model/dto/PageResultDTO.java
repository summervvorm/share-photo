package com.example.photos.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Auther: Arrow
 * @Date: 2023/5/7
 * @Description: com.example.photos.model.dto
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageResultDTO<T> {
    private List<T> data;

    private Integer count;
}

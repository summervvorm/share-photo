package com.example.photos.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Auther: Arrow
 * @Date: 2023/7/17
 * @Description: com.example.photos.model.dto
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageLabelResultDTO {
    private Integer result_num;
    private List<ResultItem> result;
    private Long log_id;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ResultItem {
        private String keyword;
        private Double score;
        private String root;
    }
}

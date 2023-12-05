package com.example.photos.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @Auther: Arrow
 * @Date: 2023/5/5
 * @Description: com.example.photos.model.dto
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailDTO {

    private String email;

    private String subject;

    private Map<String, Object> commentMap;

    private String template;

}

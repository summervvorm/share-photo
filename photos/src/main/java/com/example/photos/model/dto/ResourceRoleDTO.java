package com.example.photos.model.dto;

import lombok.Data;

import java.util.List;

/**
 * @Auther: Arrow
 * @Date: 2023/5/8
 * @Description: com.example.photos.model.dto
 */
@Data
public class ResourceRoleDTO {

    private Integer id;

    private String url;

    private String requestMethod;

    private List<String> roleList;
}

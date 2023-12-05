package com.example.photos.model.dto;

import com.example.photos.entity.Favorites;
import com.example.photos.entity.Picture;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.w3c.dom.stylesheets.LinkStyle;

import java.security.SecureRandom;
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
public class FavoritesDTO {

    private Favorites favorites;

    private Picture photos;

}

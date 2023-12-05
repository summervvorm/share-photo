package com.example.photos.service;

import com.example.photos.entity.Favorites;
import com.example.photos.entity.FavoritesList;
import com.example.photos.entity.Picture;
import com.example.photos.model.dto.FavoritesDTO;
import com.example.photos.model.dto.PageResultDTO;
import com.example.photos.model.dto.PictureDTO;
import com.example.photos.model.vo.ConditionVO;
import com.example.photos.model.vo.FavoriteVO;
import com.example.photos.model.vo.FavoritesListVO;

import java.util.List;
import java.util.Map;

/**
 * @Auther: Arrow
 * @Date: 2023/5/6
 * @Description: com.example.photos.service
 */

public interface FavoritesService {

    FavoritesList InsertFavoritesList(FavoritesListVO favoritesListVOVO);

    void InsertFavoriteLists(List<Favorites> favorites);

    PageResultDTO<PictureDTO> getFavorites(ConditionVO conditionVO);

    void deleteFavoritesImages(List<Favorites>favorites);

    List<FavoritesList>getFavListById(int userId);

    Map<String,List<FavoritesDTO>>getTypeFavListById();

    void deleteFavoritesListById(int favListId);

    void updateFavoritesList(FavoritesList favoritesList);

}

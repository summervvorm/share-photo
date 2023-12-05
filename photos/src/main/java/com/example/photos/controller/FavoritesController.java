package com.example.photos.controller;

import com.example.photos.entity.Favorites;
import com.example.photos.entity.FavoritesList;
import com.example.photos.model.vo.ConditionVO;
import com.example.photos.model.vo.FavoriteVO;
import com.example.photos.model.vo.FavoritesListVO;
import com.example.photos.model.vo.ResultVO;
import com.example.photos.service.FavoritesService;
import com.example.photos.util.UserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @Auther: Arrow
 * @Date: 2023/5/6
 * @Description: com.example.photos.controller
 */
@RestController
@Slf4j
@Api(tags = "收藏模块")
public class FavoritesController {

    @Autowired
    FavoritesService favoritesService;

    @ApiOperation("添加收藏夹")
    @PostMapping("/favorites/favoritesList")
    public ResultVO<?> insertFavorites(@RequestBody FavoritesListVO favoritesListVO){
        return ResultVO.ok(favoritesService.InsertFavoritesList(favoritesListVO));
    }
    @ApiOperation("批量添加收藏")
    @PostMapping("/favorites/imageList")
    public ResultVO<?> insertFavoritesList(@RequestBody List<Favorites> favorite){
        favoritesService.InsertFavoriteLists(favorite);
        return ResultVO.ok();
    }
    @ApiOperation("获取指定收藏夹内容")
    @GetMapping("/favorites/images")
    public ResultVO<?> getFavorites(ConditionVO conditionVO){

        return ResultVO.ok(favoritesService.getFavorites(conditionVO));
    }

    @ApiOperation("获取收藏夹列表")
    @GetMapping("/favorites/imagesList")
    public ResultVO<?> getFavorites(){
        return ResultVO.ok(favoritesService.getFavListById(UserUtil.getUserDetailsDTO().getUserId()));
    }
    @ApiOperation("获取收藏夹列表和图片详情，但是按照收藏夹名分类")
    @GetMapping("/favorites/imagesTypeList")
    public ResultVO<?> getTypeFavorites(){
        return ResultVO.ok(favoritesService.getTypeFavListById());
    }


    @ApiOperation("批量删除收藏夹图片")
    @DeleteMapping("/favorites/images")
    public ResultVO<?>delFavorites(@RequestBody List<Favorites> favorites){
        favoritesService.deleteFavoritesImages(favorites);
        return ResultVO.ok();
    }

    @ApiOperation("删除单个收藏夹")
    @DeleteMapping("/favorites/favoritesList/{favListId}")
    public ResultVO<?>delFavoritesList(@PathVariable int favListId){
        favoritesService.deleteFavoritesListById(favListId);
        return ResultVO.ok();
    }
    @ApiOperation("更改收藏夹信息")
    @PutMapping("/favorites/favoritesList")
    public ResultVO<?>updateFavoritesList(@RequestBody FavoritesList favoritesList){

        favoritesService.updateFavoritesList(favoritesList);
        return ResultVO.ok();
    }

}

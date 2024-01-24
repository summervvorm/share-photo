package com.example.photos.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.photos.entity.Favorites;
import com.example.photos.entity.FavoritesList;
import com.example.photos.entity.Picture;
import com.example.photos.exception.CommonJsonException;
import com.example.photos.mapper.FavoritesListMapper;
import com.example.photos.mapper.FavoritesMapper;
import com.example.photos.mapper.PictureMapper;
import com.example.photos.model.dto.FavoritesDTO;
import com.example.photos.model.dto.PageResultDTO;
import com.example.photos.model.dto.PictureDTO;
import com.example.photos.model.event.UserActionEvent;
import com.example.photos.model.vo.ConditionVO;
import com.example.photos.model.vo.FavoriteVO;
import com.example.photos.model.vo.FavoritesListVO;
import com.example.photos.service.FavoritesService;
import com.example.photos.service.UserService;
import com.example.photos.util.BeanCopyUtil;
import com.example.photos.util.CommonUtil;
import com.example.photos.util.PageUtil;
import com.example.photos.util.UserUtil;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Auther: Arrow
 * @Date: 2023/5/6
 * @Description: com.example.photos.service.impl
 */
@Service
@Slf4j
public class FavoritesServiceImpl implements FavoritesService {

    @Resource
    FavoritesMapper favoritesMapper;

    @Resource
    PictureMapper pictureMapper;

    @Resource
    ApplicationEventPublisher eventPublisher;

    @Resource
    FavoritesListMapper favoritesListMapper;

    @Override
    public FavoritesList InsertFavoritesList(FavoritesListVO favoritesListVO) {
        log.info(favoritesListVO.toString());
        LocalDateTime localDateTime=CommonUtil.getLocalDateTime();
        favoritesListMapper.insert(FavoritesList.builder().favName(favoritesListVO.getFavName())
                .createTime(localDateTime).favType(favoritesListVO.getFavType())
                .userId(favoritesListVO.getUserId()).build()
        );
        FavoritesList ans =favoritesListMapper.selectOne(new LambdaQueryWrapper<FavoritesList>()
                        .eq(FavoritesList::getUserId,favoritesListVO.getUserId())
                        .orderByDesc(FavoritesList::getCreateTime)
                        .last("LIMIT 1"));
        return ans;

    }

    @Override
    public void InsertFavoriteLists(List<Favorites> favorites) {
        Favorites favorites1 = favoritesMapper.selectOne(new LambdaQueryWrapper<Favorites>()
                .eq(Favorites::getFavPicId, favorites.get(0).getFavPicId())
                .eq(Favorites::getFavUserId, favorites.get(0).getFavUserId())
                .last("LIMIT 1")
        );
        if (favorites1 == null)
            pictureMapper.updatePicFav(favorites.get(0).getFavPicId(), 1);
        log.info(favorites.toString());
        UserActionEvent userActionEvent = new UserActionEvent();

        userActionEvent.setPicId(favorites.stream().map(Favorites::getFavPicId).collect(Collectors.toList()));
        userActionEvent.setUserId(favorites.get(0).getFavUserId());
        userActionEvent.setActionType(3);
        eventPublisher.publishEvent(userActionEvent);
        favoritesMapper.insertFavList(favorites);
    }

    @Override
    public PageResultDTO<PictureDTO> getFavorites(ConditionVO conditionVO) {
        Page<Picture> page = new Page<>(PageUtil.getCurrent(), PageUtil.getSize());

        List<Favorites> picList = favoritesMapper.selectList(new LambdaQueryWrapper<Favorites>()
                .eq(Favorites::getFavUserId, UserUtil.getUserDetailsDTO().getUserId())
                .eq(Favorites::getFavListId, conditionVO.getFavListId())
                .orderByDesc(Favorites::getCreateTime)
        );
        log.info("getCurrent:" + PageUtil.getCurrent() + "getSize " + PageUtil.getSize() + " listsize:" + picList.size());
        List<Integer> picIdList = new ArrayList<>();
        if(picList.size()==0){
            return null;
        }
        for (Favorites favorites : picList) {
            picIdList.add(favorites.getFavPicId());

        }
        Page<Picture> picPage = pictureMapper.selectPage(page, new LambdaQueryWrapper<Picture>()
                .in(Picture::getPicId, picIdList)
                .orderByDesc(Picture::getPicCreatTime)
        );
        List<PictureDTO> photos = BeanCopyUtil.copyList(picPage.getRecords(), PictureDTO.class);

        return new PageResultDTO<>(photos, (int) picPage.getTotal());
//        return null;
    }

    @Override
    public void deleteFavoritesImages(List<Favorites> favorites) {
        int userId = UserUtil.getUserDetailsDTO().getUserId();

        for (Favorites f : favorites) {
            favoritesMapper.delete(new LambdaQueryWrapper<Favorites>()
                    .eq(Favorites::getFavPicId, f.getFavPicId())
                    .eq(Favorites::getFavUserId, userId)
                    .in(Favorites::getFavListId, f.getFavListId())
            );
        }
        for (Favorites favorite : favorites) {
            Favorites favorites1 = favoritesMapper.selectOne(new LambdaQueryWrapper<Favorites>()
                    .eq(Favorites::getFavUserId, userId)
                    .eq(Favorites::getFavPicId, favorite.getFavPicId())
                    .last("LIMIT 1")
            );
            if (favorites1 == null) {
                pictureMapper.updatePicFav(favorite.getFavPicId(), -1);
            }

        }
        UserActionEvent userActionEvent = new UserActionEvent();
        userActionEvent.setPicId(favorites.stream().map(Favorites::getFavPicId).collect(Collectors.toList()));
        userActionEvent.setUserId(favorites.get(0).getFavUserId());
        userActionEvent.setActionType(4);
        eventPublisher.publishEvent(userActionEvent);
    }


    @Override
    public List<FavoritesList> getFavListById(int userId) {
        return new ArrayList<>(favoritesListMapper.selectList(new LambdaQueryWrapper<FavoritesList>()
                .eq(FavoritesList::getUserId,userId)
        ));
    }

    @Override
    public Map<String, List<FavoritesDTO>> getTypeFavListById() {
        int userId = UserUtil.getUserDetailsDTO().getUserId();
        Map<String, List<FavoritesDTO>> map = new HashMap<>();
        List<Favorites> list = new ArrayList<>(favoritesMapper.selectFavIdList(userId));
        for (Favorites favorites : list) {
            if (!map.containsKey(favorites.getFavCollectName())) {
                map.put(favorites.getFavCollectName(), new ArrayList<>());
            }
            Picture picture = pictureMapper.selectById(favorites.getFavPicId());
            map.get(favorites.getFavCollectName()).add(new FavoritesDTO(favorites, picture));
        }


        return map;
    }

    @Override
    public void deleteFavoritesListById(int favListId) {
//        删除收藏夹
        favoritesListMapper.delete(new LambdaQueryWrapper<FavoritesList>()
                .eq(FavoritesList::getId,favListId)
        );
//        更新收藏数
        pictureMapper.updatePicFavByDelFavList(UserUtil.getUserDetailsDTO().getUserId(), favListId);
//删除收藏数据

        UserActionEvent userActionEvent = new UserActionEvent();
        userActionEvent.setUserId(UserUtil.getUserDetailsDTO().getUserId());
        userActionEvent.setActionType(4);
        userActionEvent.setPicId(favoritesMapper.selectList(Wrappers.<Favorites>lambdaQuery().
                eq(Favorites::getFavListId,favListId)).stream().map(Favorites::getFavPicId).
                collect(Collectors.toList()));
        eventPublisher.publishEvent(userActionEvent);

        favoritesMapper.delete(new LambdaQueryWrapper<Favorites>()
                .eq(Favorites::getFavListId,favListId)
        );

    }

    @Override
    public void updateFavoritesList(FavoritesList favorites) {

        favoritesListMapper.update(FavoritesList.builder().favType(favorites.getFavType())
                .favName(favorites.getFavName()).userId(UserUtil.getUserDetailsDTO().getUserId())
                .build(),new LambdaQueryWrapper<FavoritesList>()
                .eq(FavoritesList::getId,favorites.getId())
        );
    }

}

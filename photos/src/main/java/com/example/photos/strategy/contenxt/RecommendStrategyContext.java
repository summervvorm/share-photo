package com.example.photos.strategy.contenxt;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.photos.entity.UserImageActions;
import com.example.photos.mapper.UserImageActionsMapper;
import com.example.photos.model.dto.PictureInfoDTO;
import com.example.photos.strategy.RecommendStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.example.photos.enums.RecommendModeEnum.getStrategy;

/**
 * @Auther: raolongxiang
 * @Date: 2024/1/24
 * @Description: com.example.photos.strategy.contenxt
 */
@Service
public class RecommendStrategyContext {

    @Autowired
    Map<String, RecommendStrategy>recommendStrategyMap;

    @Autowired
    UserImageActionsMapper imageActionsMapper;

    public List<PictureInfoDTO>executeRecommendStrategy(Integer userId){

        int count = Math.toIntExact(imageActionsMapper.selectCount(Wrappers.<UserImageActions>lambdaQuery().
                eq(UserImageActions::getUserId, userId)));
        String mode;
        if(count<10){
            mode ="pop";
        }else{
            mode ="contentBase";
        }
        Set<Integer>set = new HashSet<>();
        List<PictureInfoDTO>pictureInfoDTOS = recommendStrategyMap.get(getStrategy(mode)).getRecommendPic(userId);

        recommendStrategyMap.get(getStrategy(mode)).filterRecommendPic(pictureInfoDTOS,userId);

        return pictureInfoDTOS;
    }

}

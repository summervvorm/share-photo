package com.example.photos.strategy.impl;

import com.example.photos.model.dto.PictureInfoDTO;
import com.example.photos.strategy.RecommendStrategy;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: raolongxiang
 * @Date: 2024/1/24
 * @Description: com.example.photos.strategy.impl
 */
@Service("contentBaseStrategyImpl")
public class ContentBaseStrategyImpl implements RecommendStrategy {

    @Override
    public List<PictureInfoDTO> getRecommendPic(Integer userId) {
        //todo 基于内容个性化推荐


        return null;
    }
}

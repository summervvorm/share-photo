package com.example.photos.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.photos.entity.Advertisements;
import com.example.photos.enums.FilePathEnum;
import com.example.photos.mapper.AdvertisementsMapper;
import com.example.photos.model.dto.PageResultDTO;
import com.example.photos.model.dto.PictureInfoDTO;
import com.example.photos.model.vo.ConditionVO;
import com.example.photos.service.AdvertisementsService;
import com.example.photos.strategy.contenxt.UploadStrategyContext;
import com.example.photos.util.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: Arrow
 * @Date: 2023/8/10
 * @Description: com.example.photos.service.impl
 */
@Service
@Slf4j
public class AdvertisementsServiceImpl implements AdvertisementsService {


    @Resource
    AdvertisementsMapper advertisementsMapper;

    @Resource
    private UploadStrategyContext uploadStrategyContext;
    @Override
    public List<Advertisements> getRandomAD() {

        List<Advertisements>list=new ArrayList<>();
        LocalDateTime currentTime=LocalDateTime.now();

        list=advertisementsMapper.getRandList(currentTime);

        log.info(list.toString());
        return list;

    }

    @Override
    public PageResultDTO<?> getAdListByPage(ConditionVO conditionVO) {
        Page<Advertisements>page=new Page<>(PageUtil.getCurrent(), PageUtil.getSize());

        Page<Advertisements>res =advertisementsMapper.getAdvertisementList(page,conditionVO.getKeywords());
        List<Advertisements> adList = new ArrayList<>();

        if(res.getRecords().size()==0){
            return new PageResultDTO<>(adList, (int) res.getTotal());
        }


       return new PageResultDTO<>(res.getRecords(), (int) res.getTotal());
    }

    @Override
    public void deleteAdList(List<Advertisements> advertisements) {
        log.info(advertisements.toString());
        advertisementsMapper.batchDeleteAd(advertisements);
    }

    @Override
    public void insertAd(Advertisements advertisements) {
        advertisementsMapper.insert(advertisements);
    }

    @Override
    public void updateAd(Advertisements advertisements) {
        advertisementsMapper.update(advertisements,new LambdaUpdateWrapper<Advertisements>()
                .eq(Advertisements::getId,advertisements.getId())

        );
    }

    @Override
    public String uploadAdImage(MultipartFile file) {
        String path = uploadStrategyContext.executeUploadStrategy(file, FilePathEnum.ADVERTISEMENTS.getPath());

        return path;
    }
}

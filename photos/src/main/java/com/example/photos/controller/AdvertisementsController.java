package com.example.photos.controller;

import com.example.photos.entity.Advertisements;
import com.example.photos.model.vo.ConditionVO;
import com.example.photos.model.vo.ResultVO;
import com.example.photos.service.AdvertisementsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Auther: Arrow
 * @Date: 2023/8/10
 * @Description: com.example.photos.controller
 */
@RestController
@Slf4j
@Api(tags = "广告模块")
public class AdvertisementsController {

    @Resource
    AdvertisementsService advertisementsService;


    @ApiOperation("获取随机广告")
    @GetMapping("/advertisements")
    public ResultVO<?> getRandomAd() {


        return ResultVO.ok(advertisementsService.getRandomAD());
    }

    @ApiOperation("获取广告列表")
    @GetMapping("/advertisements/all")
    public ResultVO<?> getAdList(ConditionVO conditionVO) {

        return ResultVO.ok(advertisementsService.getAdListByPage(conditionVO));
    }

    @ApiOperation("批量删除广告")
    @DeleteMapping("/advertisements")
    public ResultVO<?> deleteAdvertisements(@RequestBody List<Advertisements> advertisements) {
        advertisementsService.deleteAdList(advertisements);
        return ResultVO.ok();
    }

    @ApiOperation("添加广告")
    @PostMapping("/advertisements")
    public ResultVO<?> insertAdvertisements(@RequestBody Advertisements advertisements) {
//        log.info(advertisements.toString());
        advertisementsService.insertAd(advertisements);
        return ResultVO.ok();
    }

    @ApiOperation("修改广告")
    @PutMapping("/advertisements")
    public ResultVO<?> updateAdvertisements(@RequestBody Advertisements advertisements) {
        log.info(advertisements.toString());
        advertisementsService.updateAd(advertisements);
        return ResultVO.ok();
    }

    @ApiOperation("上传广告图片")
    @PostMapping("/advertisementImages")
    public ResultVO<?> uploadAdImage(MultipartFile file) {


        return ResultVO.ok(advertisementsService.uploadAdImage(file));
    }


}

package com.example.photos.controller;

import com.example.photos.entity.LevelResource;
import com.example.photos.model.vo.ConditionVO;
import com.example.photos.model.vo.ResultVO;
import com.example.photos.service.LevelService;
import com.example.photos.service.PictureService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: Arrow
 * @Date: 2023/7/10
 * @Description: com.example.photos.controller
 */
@RestController
@Slf4j
@Api(tags = "等级模块")
public class LevelController {

    @Resource
    LevelService levelService;

    @Resource
    PictureService pictureService;

    @ApiOperation("获取全部等级信息和用户的下载量")
    @GetMapping("/levels")
    public ResultVO<?> getAllLevel() {
        Map<String, Object> map = new HashMap<>();
        int count = pictureService.downloadPic(0, -1L);
        map.put("count", count);
        map.put("level", levelService.getAllLevelResource());
        return ResultVO.ok(map);
    }

    @ApiOperation("获取全部等级信息")
    @GetMapping("/allLevels")
    public ResultVO<?> getLevels(ConditionVO conditionVO) {

        return ResultVO.ok(levelService.getPageLevel(conditionVO));
    }

    @ApiOperation("修改等级信息")
    @PostMapping("/levels")
    public ResultVO<?>updateLevel(@RequestBody LevelResource levelResource){
        levelService.updateLevel(levelResource);
        return ResultVO.ok();
    }

    @ApiOperation("新增等级信息")
    @PostMapping("/addLevels")
    public ResultVO<?>addLevel(@RequestBody LevelResource levelResource){
        levelService.addLevel(levelResource);
        return ResultVO.ok();
    }

}

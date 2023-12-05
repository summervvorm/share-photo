package com.example.photos.controller;

import com.example.photos.model.dto.ReportTagsDTO;
import com.example.photos.model.vo.ConditionVO;
import com.example.photos.model.vo.ReportTagsVO;
import com.example.photos.model.vo.ResultVO;
import com.example.photos.service.PictureTagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Auther: Arrow
 * @Date: 2023/6/7
 * @Description: com.example.photos.controller
 */
@RestController
@Slf4j
@Api(tags = "标签模块")
public class TagController {

    @Resource
    PictureTagService pictureTagService;

    @ApiOperation("报告标签")
    @PostMapping("/reportTags")
    public ResultVO<?> reportTags(@RequestBody ReportTagsVO reportTagsVO) {
        log.info(reportTagsVO.toString());
        pictureTagService.addReportTag(reportTagsVO);
        return ResultVO.ok();
    }

    @ApiOperation("获取报告标签列表")
    @GetMapping("/reportTags")
    public ResultVO<?> getReportTags(ConditionVO conditionVO) {
//        log.info(pictureTagService.getReportTagsList(conditionVO).toString());
        return ResultVO.ok(pictureTagService.getReportTagsList(conditionVO));
    }

    @ApiOperation("通过反馈标签")
    @PostMapping("/tags")
    public ResultVO<?>passTags(@RequestBody List<ReportTagsDTO> reportTagsDTOList){
        pictureTagService.addTagsByReportTags(reportTagsDTOList);
        return ResultVO.ok();
    }

    @ApiOperation("删除反馈标签")
    @DeleteMapping("/reportTags")
    public ResultVO<?>deleteReportTags(@RequestBody List<ReportTagsDTO> reportTagsDTOList){
        pictureTagService.deleteReportTags(reportTagsDTOList);
        return ResultVO.ok();
    }

    @ApiOperation("获取标签云图数据")
    @GetMapping("/tagsWordClouds")
    public ResultVO<?>selectTagsWordCloudDate(){
        return ResultVO.ok(pictureTagService.selectTagsWordCloudDate());
    }


    @ApiOperation("自动审核标签")
    @PostMapping("/auto/reportTags")
    public ResultVO<?>AutoAuditTags(@RequestBody ReportTagsDTO reportTagsDTO){
        return ResultVO.ok(pictureTagService.autoAudit(reportTagsDTO));
    }


}

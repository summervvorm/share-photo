package com.example.photos.controller;

import com.example.photos.model.dto.PictureInfoDTO;
import com.example.photos.model.dto.ReportPicDTO;
import com.example.photos.model.dto.ReportPicDTO;
import com.example.photos.model.vo.ConditionVO;
import com.example.photos.model.vo.PictureUploadVO;
import com.example.photos.model.vo.ReportPicVO;
import com.example.photos.model.vo.ResultVO;
import com.example.photos.service.PictureService;
import com.example.photos.service.RedisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @Auther: Arrow
 * @Date: 2023/5/11
 * @Description: com.example.photos.controller
 */
@RestController
@Slf4j
@Api(tags = "图片模块")
public class PictureController {

    @Resource
    PictureService pictureService;


    @ApiOperation("根据图片Id查询单张图片")
    @GetMapping("/picture/images/{picId}")
    public ResultVO<?> getPicInfoById(@Valid @PathVariable Integer picId) {

        return ResultVO.ok(pictureService.getPicInfoById(picId));

    }

    @ApiOperation("根据多个条件查询图片并分页")
    @GetMapping("/picture/imagespage")
    public ResultVO<?> getPicPageByCondition(ConditionVO conditionVO) {
        return ResultVO.ok(pictureService.getPicPageByCondition(conditionVO));
    }

    @ApiOperation("个性化推荐图片")
    @GetMapping("/picture/recommend/{userId}")
    public ResultVO<?>getRecommendPic(@PathVariable Integer userId){

        return ResultVO.ok(pictureService.getRecommendPic(userId));
    }

    @ApiOperation("上传图片")
    @PostMapping("/picture/upload")
    public ResultVO<?> uploadPic(PictureUploadVO pictureUploadVO) {
        log.info(pictureUploadVO.toString());
        pictureService.uploadPic(pictureUploadVO);
        return ResultVO.ok();
    }

    @ApiOperation("根据ID删除图片")
    @DeleteMapping("/picture/images/{picId}")
    public ResultVO<?> deletePicById(@Valid @PathVariable Integer picId) {
        pictureService.deletePicById(picId);
        return ResultVO.ok();
    }

    @ApiOperation("检查用户是否点赞或收藏图片")
    @GetMapping("/picture/images/check/{userId}/{picId}")
    public ResultVO<?> checkUserLikesOrFavPic(@Valid @PathVariable int userId, @PathVariable int picId) {
        return ResultVO.ok(pictureService.checkUserLikesOrFavPic(userId, picId));
    }

    @ApiOperation("首页图片获取")
    @GetMapping("/picture/typeImages{picType}")
    public ResultVO<?> getHomePic(@Valid @PathVariable String picType) {
        return ResultVO.ok(pictureService.getPicInfoByType(picType));
    }

    @ApiOperation("点赞图片")
    @PostMapping("/picture/images/like/{userId}/{picId}")
    public ResultVO<?> likePic(@PathVariable int userId, @PathVariable int picId) {
        pictureService.insertPicLike(userId, picId);
        return ResultVO.ok();
    }

    @ApiOperation("获取用户上传的图片")
    @GetMapping("/picture/images/upload/{userId}")
    public ResultVO<?> uploadPic(ConditionVO conditionVO, @PathVariable int userId) {

        return ResultVO.ok(pictureService.getUploadPicList(conditionVO,userId));
    }

    @ApiOperation("获取用户点赞和收藏图片的列表")
    @GetMapping("/picture/likeAndFav/images")
    public ResultVO<?> getLikeAndFavPicIdList() {
        return ResultVO.ok(pictureService.getLikeAndFavPicIdList());
    }

    @ApiOperation("取消点赞")
    @DeleteMapping("/picture/images/like/{userId}/{picId}")
    public ResultVO<?> deleteLikePic(@PathVariable int userId, @PathVariable int picId) {
        pictureService.deletePicLike(userId, picId);
        return ResultVO.ok();
    }

    @ApiOperation("反馈图片问题")
    @PostMapping("/picture/reportPictures")
    public ResultVO<?> feedbackPic(@RequestBody ReportPicVO reportPicVO) {

        pictureService.addReportPic(reportPicVO);

        return ResultVO.ok();
    }

    @ApiOperation("获取反馈图片")
    @GetMapping("/picture/reportPictures")
    public ResultVO<?> getReportPic(ConditionVO conditionVO) {

        log.info(conditionVO.toString());
        return ResultVO.ok(pictureService.getReportPicList(conditionVO));
    }

    @ApiOperation("下架反馈图片")
    @PostMapping("/picture/picturesAndReportPic")
    public ResultVO<?> passReportPic(@RequestBody List<ReportPicDTO> reportPicDTOList) {
        pictureService.takeDownPicture(reportPicDTOList);
        return ResultVO.ok();
    }

    @ApiOperation("删除反馈图片")
    @DeleteMapping("/picture/reportPictures")
    public ResultVO<?> deleteReportPic(@RequestBody List<ReportPicDTO> reportPicDTOList) {
        pictureService.deleteReportPic(reportPicDTOList);
        return ResultVO.ok();
    }

    @ApiOperation("下载图片")
    @PostMapping("/picture/download/{i}&{picId}")
    public ResultVO<?> download(@PathVariable int i, @PathVariable Long picId) {
        return ResultVO.ok(pictureService.downloadPic(i,picId));
    }


    @ApiOperation("更新图片信息")
    @PostMapping("/picture/pictures")
    public ResultVO<?> updatePicInfo(@RequestBody PictureInfoDTO pictureInfoDTO){
        pictureService.updatePictureInfo(pictureInfoDTO);
        log.info(pictureInfoDTO.toString());
        return ResultVO.ok();
    }

    @ApiOperation("获取图片上传统计数据")
    @GetMapping("/picture/pictureStats")
    public ResultVO<?> getUpdatePic(){

        return ResultVO.ok(pictureService.getPictureStatsDTO());
    }

    @ApiOperation("获取用户量，标签，图片，评论数量")
    @GetMapping("/picture/dataCounts")
    public ResultVO<?>getDataCount(){

        return ResultVO.ok(pictureService.getDataCount());
    }

    @ApiOperation("通过审核")
    @PostMapping("/picture/auditPics")
    public ResultVO<?> passAuditPic(@RequestBody List<PictureInfoDTO>pictureInfoDTOList){
        pictureService.passAuditPic(pictureInfoDTOList);
        return ResultVO.ok();
    }
    @ApiOperation("不通过审核")
    @DeleteMapping("/picture/auditPics")
    public ResultVO<?> deleteAuditPic(@RequestBody List<PictureInfoDTO>pictureInfoDTOList){
        pictureService.refuseAuditPic(pictureInfoDTOList);
        return ResultVO.ok();
    }
}

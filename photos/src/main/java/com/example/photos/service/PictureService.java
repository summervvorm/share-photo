package com.example.photos.service;

import com.example.photos.entity.Picture;
import com.example.photos.model.dto.PageResultDTO;
import com.example.photos.model.dto.PictureInfoDTO;
import com.example.photos.model.dto.PictureStatsDTO;
import com.example.photos.model.dto.ReportPicDTO;
import com.example.photos.model.vo.ConditionVO;
import com.example.photos.model.vo.PictureUploadVO;
import com.example.photos.model.vo.ReportPicVO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Auther: Arrow
 * @Date: 2023/5/4
 * @Description: com.example.photos.service
 */
public interface PictureService {
    PictureInfoDTO getPicInfoById(Integer picId);

    PageResultDTO<?> getPicPageByCondition(ConditionVO conditionVO);

    String uploadPic(PictureUploadVO pictureUploadVO);

    void deletePicById(Integer picId);

    List<PictureInfoDTO> getPicInfoByType(String type);

    Map<String, BigDecimal> countLikesAndFavoritesByUserId(int userId);

    Map<String, Boolean> checkUserLikesOrFavPic(int userId, int picId);

    int insertPicLike(int userId, int picId);

    int deletePicLike(int userId, int picId);

    Map<String, List<Integer>> getLikeAndFavPicIdList();

    PageResultDTO<?> getUploadPicList(ConditionVO conditionVO,int userId);


    public PageResultDTO<?> getReportPicList(ConditionVO conditionVO);

    public void takeDownPicture(List<ReportPicDTO> reportPicDTOList);

    public void deleteReportPic(List<ReportPicDTO> reportPicDTOList);

    public void  addReportPic(ReportPicVO reportPicVO);

    public int downloadPic(int i,Long picId);

    void updatePictureInfo(PictureInfoDTO pictureInfoDTO);

    List<PictureStatsDTO> getPictureStatsDTO();


    List<Integer>getDataCount();

    void passAuditPic(List<PictureInfoDTO> pictureInfoDTOList);

    void refuseAuditPic(List<PictureInfoDTO>pictureInfoDTOList);

    List<PictureInfoDTO>getRecommendPic(Integer userId);

}

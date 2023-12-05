package com.example.photos.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.photos.entity.Picture;
import com.example.photos.entity.Tag;
import com.example.photos.model.dto.PageResultDTO;
import com.example.photos.model.dto.PictureTagsWordCloudDTO;
import com.example.photos.model.dto.ReportTagsDTO;
import com.example.photos.model.vo.ConditionVO;
import com.example.photos.model.vo.PictureUploadVO;
import com.example.photos.model.vo.ReportTagsVO;

import java.util.List;
import java.util.Map;

/**
 * @Auther: Arrow
 * @Date: 2023/5/31
 * @Description: com.example.photos.service
 */

public interface PictureTagService {
    void addPicTag(PictureUploadVO pictureUploadVO,int picId);

    Map<Integer,List<Tag>> getTagsByPicList(List<Picture> pictures);

    List<Tag>getTagByPic(Picture picture);

    void addReportTag(ReportTagsVO reportTagsVO);

    PageResultDTO<?> getReportTagsList(ConditionVO conditionVO);

    void addTagsByReportTags(List<ReportTagsDTO> reportTagsDTOList);

    void deleteReportTags(List<ReportTagsDTO>reportTagsDTOList);

    List<PictureTagsWordCloudDTO> selectTagsWordCloudDate();

    boolean autoAudit(ReportTagsDTO reportTagsDTO);


}

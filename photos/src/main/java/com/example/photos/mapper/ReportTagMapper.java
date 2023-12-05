package com.example.photos.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.photos.entity.Picture;
import com.example.photos.entity.ReportTag;
import com.example.photos.model.dto.ReportTagsDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Auther: Arrow
 * @Date: 2023/7/3
 * @Description: com.example.photos.mapper
 */
@Repository
@Mapper
public interface ReportTagMapper extends BaseMapper<ReportTag> {
    void batchInsertReportTags(List<ReportTag> reportTags);

    Page<ReportTagsDTO>getReportTagsList(Page<ReportTagsDTO> page,@Param("keyword")String keywords);

    void batchDeleteReportTag(List<ReportTagsDTO> reportTagsDTOList);
}

package com.example.photos.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.photos.entity.ReportPic;
import com.example.photos.model.dto.ReportPicDTO;
import com.example.photos.model.dto.ReportTagsDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Auther: Arrow
 * @Date: 2023/7/6
 * @Description: com.example.photos.mapper
 */
@Mapper
@Repository
public interface ReportPicMapper extends BaseMapper<ReportPic> {

    Page<ReportPicDTO> getReportPicList(Page<ReportPicDTO> page, @Param("keyword")String keywords);

    void batchDeleteReportPic(List<ReportPicDTO> reportPicDTOList);
}

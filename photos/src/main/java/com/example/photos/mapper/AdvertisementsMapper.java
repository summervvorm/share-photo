package com.example.photos.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.photos.entity.Advertisements;
import com.example.photos.entity.Picture;
import com.example.photos.model.dto.ReportPicDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Auther: Arrow
 * @Date: 2023/8/10
 * @Description: com.example.photos.mapper
 */
@Mapper
@Repository

public interface AdvertisementsMapper extends BaseMapper<Advertisements> {

    Page<Advertisements> getAdvertisementList(Page<Advertisements> page, @Param("keyword") String keyword);

    void batchDeleteAd(List<Advertisements> advertisements);

    List<Advertisements>getRandList(LocalDateTime currentTime);

}

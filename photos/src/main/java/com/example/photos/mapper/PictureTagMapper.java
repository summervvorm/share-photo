package com.example.photos.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.photos.model.dto.PictureTagsWordCloudDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import com.example.photos.entity.PictureTag;

import java.util.List;

/**
 * @Auther: Arrow
 * @Date: 2023/5/31
 * @Description: com.example.photos.mapper
 */
@Repository
@Mapper
public interface PictureTagMapper extends BaseMapper<PictureTag> {
    int insertOne(PictureTag pictureTag);

    void insertList(List<PictureTag> pictureTags);


    List<PictureTagsWordCloudDTO> selectTagsWordCloudDate();

}

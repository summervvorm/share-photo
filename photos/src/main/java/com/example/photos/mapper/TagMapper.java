package com.example.photos.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.photos.entity.Picture;
import com.example.photos.entity.PictureTag;
import com.example.photos.entity.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Auther: Arrow
 * @Date: 2023/5/31
 * @Description: com.example.photos.mapper
 */
@Repository
@Mapper
public interface TagMapper extends BaseMapper<Tag> {
    int insertTagsIfNotExists(List<String> tagNameList);

    List<Tag> selectTagsId(List<String> tagNames);

    List<PictureTag>selectTagsByPicIds(@Param("pictures") List<Picture>pictures);
}

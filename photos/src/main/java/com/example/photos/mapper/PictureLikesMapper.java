package com.example.photos.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.photos.entity.PictureLikes;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Auther: Arrow
 * @Date: 2023/6/14
 * @Description: com.example.photos.mapper
 */
@Repository
@Mapper
public interface PictureLikesMapper extends BaseMapper<PictureLikes> {

    List<Integer>getPicIdListByUserId(int userId);

}

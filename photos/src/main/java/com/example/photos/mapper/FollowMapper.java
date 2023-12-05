package com.example.photos.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.photos.entity.Follow;
import com.example.photos.model.dto.FansDTO;
import com.example.photos.model.dto.FollowDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Auther: Arrow
 * @Date: 2023/5/7
 * @Description: com.example.photos.mapper
 */
@Mapper
@Repository
public interface FollowMapper extends BaseMapper<Follow> {

    List<Follow>selectFollowList(int userId);

    List<Follow>selectFansList(int userId);

    int selectFansCount(int userId);
// 查找相应用户粉丝列表
    List<FansDTO>selectFansDTOList(int userId);

    List<FollowDTO>selectFollowTOList(int userId);
}

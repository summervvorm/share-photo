package com.example.photos.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.photos.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Auther: Arrow
 * @Date: 2023/5/10
 * @Description: com.example.photos.mapper
 */
@Mapper
@Repository
public interface UserRoleMapper extends BaseMapper<UserRole> {
}

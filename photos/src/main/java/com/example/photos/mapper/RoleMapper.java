package com.example.photos.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.photos.entity.Role;
import com.example.photos.model.dto.ResourceRoleDTO;
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
public interface RoleMapper extends BaseMapper<Role> {
    List<String>selectListRoles(int userId);

    List<ResourceRoleDTO>listResourceRoles();
}

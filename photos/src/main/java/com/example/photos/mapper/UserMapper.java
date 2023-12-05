package com.example.photos.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.photos.entity.Picture;
import com.example.photos.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * @Auther: Arrow
 * @Date: 2023/4/24
 * @Description: com.example.photos.mapper
 */
@Repository
@Mapper
public interface UserMapper extends BaseMapper<User> {

    User checkUser(String username, String password);

    String checkEmails(String email);

    void addExperience(int userId);

    Page<User> getUserInfoList(Page<User> page, @Param("keyword") String keyword);

    void updateUserExperience(int userId,int experience);

}

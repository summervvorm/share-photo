package com.example.photos.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.photos.entity.Follow;
import com.example.photos.entity.User;
import com.example.photos.mapper.FavoritesMapper;
import com.example.photos.mapper.FollowMapper;
import com.example.photos.mapper.RoleMapper;
import com.example.photos.mapper.UserMapper;
import com.example.photos.model.dto.UserDetailsDTO;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @Auther: Arrow
 * @Date: 2023/5/7
 * @Description: com.example.photos.service.impl
 */
@Slf4j
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Resource
    UserMapper userMapper;

    @Resource
    FollowMapper followMapper;

    @Resource
    RoleMapper roleMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isBlank(username)) {
            throw new UsernameNotFoundException("用户名不能为空!");
        }
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUserEmail,username)
        );
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("用户不存在!");
        }
        log.info("触发了：loadUserByUsername");
        return convertUserDetail(user);
    }
    public UserDetailsDTO convertUserDetail(User user) {
        log.info("这是user: "+user);
        List<Follow>followList = followMapper.selectFollowList(user.getUserId());
        List<Follow>fansList = followMapper.selectFansList(user.getUserId());
        List<String>roles = roleMapper.selectListRoles(user.getUserId());
        UserDetailsDTO userDetailsDTO=UserDetailsDTO.builder().fansListId(fansList).followListId(followList)
                .header(user.getUserHeaderImage()).UserId(user.getUserId()).userEmail(user.getUserEmail())
                .lastLoginTime(user.getCreateTime()).nickname(user.getUserNickname())
                .password( user.getUserPassword()).sign(user.getUserSign()).rolesList(roles)
                .username(user.getUserEmail())
                .experience(user.getExperience())
                .build();
        log.info(userDetailsDTO.toString());
        return userDetailsDTO;
    }

}

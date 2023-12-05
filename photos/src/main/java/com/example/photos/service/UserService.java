package com.example.photos.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.example.photos.entity.User;
import com.example.photos.model.dto.*;
import com.example.photos.model.vo.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Auther: Arrow
 * @Date: 2023/4/24
 * @Description: com.example.photos.service
 */
public interface UserService{

    User getUserInfoById(int userId);

    boolean checkUser(UserVO userVO);

    void updateUserInfo(UserInfoVO userInfoVO);

    String updateUserHeader(MultipartFile file);

    UserDetailsDTO getCurrentUser();

    void registerUser(UserVO userVO);

    void deleteUserById(int userId);

    void deleteUserByList(List<Integer>  userList);

    void sendEmailCode(String userEmail);

    String logoutUser();

    boolean checkUserIsExist(int userId);

    boolean checkEmails(String userEmail);

    AuthorInfoDTO getAuthorInfoById(int userId);

    void followUser(FollowVO followVO);

    boolean checkFollow(int userId ,int fansId);

   List<FansDTO> getFansList();

   List<FollowDTO>getFollowList();

   String updatePassword(PasswordVO passwordVO);

   PageResultDTO getAllUserInfo(ConditionVO conditionVO);

   void updateUserExperience(int userId,int experience);

   void forgetPasswords(String password,String code);

}

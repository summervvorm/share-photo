package com.example.photos.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.photos.entity.*;
import com.example.photos.enums.FilePathEnum;
import com.example.photos.enums.StatusCodeEnum;
import com.example.photos.exception.CommonJsonException;
import com.example.photos.mapper.FavoritesListMapper;
import com.example.photos.mapper.FollowMapper;
import com.example.photos.mapper.UserMapper;
import com.example.photos.mapper.UserRoleMapper;
import com.example.photos.model.dto.*;
import com.example.photos.model.vo.*;
import com.example.photos.service.PictureService;
import com.example.photos.service.RedisService;
import com.example.photos.service.TokenService;
import com.example.photos.service.UserService;
import com.example.photos.strategy.contenxt.UploadStrategyContext;
import com.example.photos.util.CommonUtil;
import com.example.photos.util.PageUtil;
import com.example.photos.util.UserUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import javax.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.example.photos.constant.AuthConstant.EXPIRE_TIME;
import static com.example.photos.constant.CommonConstant.ROLE_USER_ID;
import static com.example.photos.constant.RabbitMQConstant.EMAIL_EXCHANGE;
import static com.example.photos.constant.RedisConstant.*;
import static com.example.photos.enums.StatusCodeEnum.*;
import static com.example.photos.util.CommonUtil.checkEmail;
import static com.example.photos.util.CommonUtil.getRandomCode;
import static com.example.photos.util.UserUtil.getUserDetailsDTO;

/**
 * @Auther: Arrow
 * @Date: 2023/4/24
 * @Description: com.example.photos.service.impl
 */

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private UploadStrategyContext uploadStrategyContext;


    @Resource
    private RedisService redisService;

    @Resource
    private HttpServletRequest request;

    @Resource
    private HttpServletResponse response;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private PictureService pictureService;

    @Resource
    private TokenService tokenService;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private FollowMapper followMapper;

    @Resource
    private UserDetailServiceImpl userDetailsService;

    @Resource
    private FavoritesListMapper favoritesListMapper;

    @Override
    public User getUserInfoById(int userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
//            throw new CommonJsonException(StatusCodeEnum.USERNAME_NOT_EXIST);
        }
        return user;
    }

    @Override
    public boolean checkUser(UserVO userVO) {
        User user = userMapper.checkUser(userVO.getUsername(), userVO.getPassword());
        if (user == null) {
            throw new CommonJsonException(StatusCodeEnum.LOGIN_ERROR);
        }
        String userCode = getRandomCode();
        String userKey = user.getUserId() + userCode;
        redisService.set(userKey, JSONObject.toJSONString(user));
        redisService.hSet(LOGIN_USER, user.getUserId() + "", UserDetailsDTO.builder());

        return true;
    }

    @Override
    public void updateUserInfo(UserInfoVO userInfoVO) {
        User user = User.builder().userId(userInfoVO.getUserId()).
                userSign(userInfoVO.getSign()).
                userNickname(userInfoVO.getNickname()).
                build();
        userMapper.updateById(user);
    }

    @Override
    public String updateUserHeader(MultipartFile file) {
        String header = uploadStrategyContext.executeUploadStrategy(file, FilePathEnum.AVATAR.getPath());
        UserDetailsDTO userDetailsDTO = getUserDetailsDTO();
        User user = User.builder().userId(userDetailsDTO.getUserId()).userHeaderImage(header).build();
        userMapper.updateById(user);
        return header;
    }

    @Override
    public UserDetailsDTO getCurrentUser() {
        if (UserUtil.getUserDetailsDTO().getUsername() == null) {
            return null;
        }
        int userId = UserUtil.getUserDetailsDTO().getUserId();
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUserId, userId)
        );
        UserDetailsDTO userDetailsDTO = userDetailsService.convertUserDetail(user);
        LocalDateTime currentTime = LocalDateTime.now();
        userDetailsDTO.setExpireTime(currentTime.plusSeconds(EXPIRE_TIME));
        userDetailsDTO.setPassword("");
        String UserDetailDTOJSON = JSON.toJSONString(userDetailsDTO);
        redisService.hSet(LOGIN_USER, userDetailsDTO.getUsername(), UserDetailDTOJSON, EXPIRE_TIME);
        return userDetailsDTO;


    }

    @Override
    public void registerUser(UserVO userVO) {
        User user = User.builder()
                .userPassword(BCrypt.hashpw(userVO.getPassword(), BCrypt.gensalt()))
                .createTime(CommonUtil.getLocalDateTime())
                .userNickname(userVO.getNickname())
                .userEmail(userVO.getUsername()).build();
        log.info("这是注册的用户信息:  " + user.toString());
        if (getUserInfoById(user.getUserId()) != null) {
            throw new CommonJsonException(USERNAME_EXIST);
        }
        String code = (String) redisService.get(USER_CODE_KEY + user.getUserEmail());
        log.info("用户输入的验证码为：" + code);
        userMapper.insert(user);
        UserRole userRole = new UserRole();
        User user1 = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUserEmail, user.getUserEmail())
        );
        UserRole.builder().userId(user1.getUserId()).roleId(ROLE_USER_ID).build();
        favoritesListMapper.insert(FavoritesList.builder().userId(user1.getUserId()).createTime(CommonUtil.getLocalDateTime()).build());
        userRoleMapper.insert(userRole);
    }

    @Override
    public void deleteUserById(int userId) {

        int ans = userMapper.deleteById(userId);

        if (ans == 0) {
            throw new CommonJsonException(StatusCodeEnum.USERNAME_NOT_EXIST);
        }

    }

    @Override
    public void deleteUserByList(List<Integer> userList) {

        if (userList.isEmpty()) {
            throw new CommonJsonException(StatusCodeEnum.VALID_ERROR);
        }

        int ans = userMapper.deleteBatchIds(userList);
        if (ans == 0) {
            throw new CommonJsonException(StatusCodeEnum.USERNAME_NOT_EXIST);
        }
    }

    @Override
    public void sendEmailCode(String userEmail) {
        if (!checkEmail(userEmail)) {
            throw new CommonJsonException(StatusCodeEnum.VALID_ERROR);
        }
        String code = getRandomCode();
        Map<String, Object> map = new HashMap<>();
        map.put("content", "您的验证码为 " + code + " 有效期15分钟，请不要告诉他人哦！");
        EmailDTO emailDTO = EmailDTO.builder()
                .email(userEmail)
                .subject("验证码")
                .template("common.html")
                .commentMap(map)
                .build();
        rabbitTemplate.convertAndSend(EMAIL_EXCHANGE, "*", new Message(
                JSON.toJSONBytes(emailDTO), new MessageProperties()));
        redisService.set(USER_CODE_KEY + userEmail, code, CODE_EXPIRE_TIME);
        log.info("已发送验证码：" + code);
    }

    @Override
    @SneakyThrows
    public String logoutUser() {
        log.info("注销用户");
        tokenService.delLoginUser(getUserDetailsDTO().getUsername());

        return "注销成功";
    }

    @Override
    public boolean checkUserIsExist(int userId) {
//        log.info("执行了checkUserIsExist");
        User user = userMapper.selectById(userId);
        if (Objects.isNull(user))
            return false;
        return true;
    }

    @Override
    public boolean checkEmails(String userEmail) {
        String ans = userMapper.checkEmails(userEmail);
        if (ans == null || ans.isEmpty())
            return true;
        return false;
    }

    @Override
    public AuthorInfoDTO getAuthorInfoById(int userId) {
        User user = userMapper.selectById(userId);
        if(user == null){
            throw new CommonJsonException(USERNAME_NOT_EXIST);
        }
        Map<String, BigDecimal> map = pictureService.countLikesAndFavoritesByUserId(userId);
        int likes = map.get("likes_count").intValue();
        int fav = map.get("favorites_count").intValue();
        int fansCount = followMapper.selectFansCount(userId);
        AuthorInfoDTO authorInfoDTO = AuthorInfoDTO.builder()
                .userId(userId)
                .favCount(fav)
                .likesCount(likes)
                .header(user.getUserHeaderImage())
                .nickname(user.getUserNickname())
                .sign(user.getUserSign())
                .fansCount(fansCount)
                .createTime(user.getCreateTime())
                .build();
        log.info("这是authorInfo: "+authorInfoDTO);
        return authorInfoDTO;
    }

    @Override
    public void followUser(FollowVO followVO) {
        log.info(followVO.toString());
        if (followVO.isToFollow()) {
            followMapper.insert(Follow.builder().
                    folUserId(followVO.getUserId()).
                    folFansId(followVO.getFansId()).
                    CreateTime(LocalDateTime.now()).
                    build());
        } else {
            followMapper.delete(new LambdaQueryWrapper<Follow>()
                    .eq(Follow::getFolUserId, followVO.getUserId())
                    .eq(Follow::getFolFansId, followVO.getFansId())
            );
        }
    }

    @Override
    public boolean checkFollow(int userId, int fansId) {
        Follow follow = followMapper.selectOne(new LambdaQueryWrapper<Follow>()
                .eq(Follow::getFolFansId, fansId)
                .eq(Follow::getFolUserId, userId)
        );
        if (follow == null)
            return false;
        return true;
    }

    @Override
    public List<FansDTO> getFansList() {
        return followMapper.selectFansDTOList(UserUtil.getUserDetailsDTO().getUserId());
    }

    @Override
    public List<FollowDTO> getFollowList() {
        return followMapper.selectFollowTOList(UserUtil.getUserDetailsDTO().getUserId());
    }

    @Override
    public String updatePassword(PasswordVO passwordVO) {
       User user=userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUserId,UserUtil.getUserDetailsDTO().getUserId())
        );
//       log.info(String.valueOf(passwordVO));
        boolean flag = new BCryptPasswordEncoder().matches(passwordVO.getOldPassword(),user.getUserPassword());
        if(flag){
            user.setUserPassword(BCrypt.hashpw(passwordVO.getNewPassword(), BCrypt.gensalt()));
            userMapper.update(user,new LambdaQueryWrapper<User>()
                    .eq(User::getUserId,UserUtil.getUserDetailsDTO().getUserId()));

        }else{
            throw new CommonJsonException(FAIL);
        }

        return "修改成功";
    }

    @Override
    public PageResultDTO getAllUserInfo(ConditionVO conditionVO) {
        Page<User> page = new Page<>(PageUtil.getCurrent(), PageUtil.getSize());

        Page<User>res=userMapper.getUserInfoList(page,conditionVO.getKeywords());

//        log.info(conditionVO.toString());

//        log.info(res.getRecords().toString());

        return new PageResultDTO<>(res.getRecords(), (int) res.getTotal());


//        return null;
    }

    @Override
    public void updateUserExperience(int userId, int experience) {
        userMapper.updateUserExperience(userId,experience);
    }

    @Override
    public void forgetPasswords(String  password, String code) {
        UserDetailsDTO user = UserUtil.getUserDetailsDTO();

        String codes= (String) redisService.get(USER_CODE_KEY + user.getUserEmail());
        log.info(codes+" "+code);
        password=(BCrypt.hashpw(password, BCrypt.gensalt()));
        if(codes!=null&&codes.equals(code)){

            userMapper.update(null, new LambdaUpdateWrapper<User>()
                    .set(User::getUserPassword, password)
                    .eq(User::getUserId, user.getUserId())
            );

            logoutUser();

        }else{
            throw new CommonJsonException("验证码错误");
        }


    }


}
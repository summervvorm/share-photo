package com.example.photos.controller;

import com.example.photos.annotation.AccessLimit;
import com.example.photos.entity.User;
import com.example.photos.enums.StatusCodeEnum;
import com.example.photos.exception.CommonJsonException;
import com.example.photos.model.dto.AuthorInfoDTO;
import com.example.photos.model.vo.*;
import com.example.photos.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @Auther: Arrow
 * @Date: 2023/4/24
 * @Description: com.example.photos.controller
 */
@RestController
@Slf4j
@Api(tags = "用户模块")
@RequestMapping("users")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("info")
    @ApiOperation("根据token获取用户信息")
    public ResultVO<?>getUserInfoByToken(){
        return ResultVO.ok(userService.getCurrentUser());
    }


    @GetMapping("info/{userId}")
    @ApiOperation("根据id获取用户信息")
    public ResultVO<User>getUserInfoById(@Valid@PathVariable("userId") Integer userId){
        User user = userService.getUserInfoById(userId);
        if(user==null){
            throw new CommonJsonException(StatusCodeEnum.USERNAME_NOT_EXIST);
        }
        return ResultVO.ok(user);
    }
    @GetMapping("author/{userId}")
    @ApiOperation("获取作者信息")
    public ResultVO<AuthorInfoDTO>getAuthorInfo(@Valid@PathVariable("userId")Integer userId){
        return ResultVO.ok(userService.getAuthorInfoById(userId));
    }


    @PutMapping("users/update")
    @ApiOperation("更新用户信息")
    public ResultVO<?>updateUser(@Valid@RequestBody UserInfoVO userInfoVO){
        userService.updateUserInfo(userInfoVO);
        return ResultVO.ok();
    }

    @ApiOperation("更改用户经验")
    @PutMapping("experiences/{userId}/{experience}")
    public ResultVO<?>updateUserExperience(@PathVariable int experience, @PathVariable int userId){
        userService.updateUserExperience(userId,experience);
        return ResultVO.ok();
    }

    @ApiOperation("更新用户头像")
    @ApiImplicitParam(name = "file",value = "用户头像",readOnly = true,dataType = "MultipartFile")
    @PostMapping("header")
    public ResultVO<String>updateUserHeader(MultipartFile file){
        log.info("headerflie:"+file);
        return ResultVO.ok(userService.updateUserHeader(file));
    }

    @ApiOperation("修改密码")
    @PostMapping("password")
    public ResultVO<?>updatePassword(@RequestBody PasswordVO passwordVO){
        log.info(passwordVO.toString());
        return ResultVO.ok(userService.updatePassword(passwordVO));
    }

    @ApiOperation("用户注册")
    @PostMapping("register")
    public ResultVO<?>registerUser(@Valid@RequestBody UserVO userVO){
        userService.registerUser(userVO);
        return ResultVO.ok();
    }

    @AccessLimit(seconds = 60,maxCount = 1)
    @ApiOperation(value = "发送邮箱验证码")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String")
    @GetMapping("code")
    public ResultVO<?>sendCode(String userEmail){
        userService.sendEmailCode(userEmail);
        return ResultVO.ok();
    }

    @ApiOperation(value = "检查邮箱是否存在")
    @GetMapping("emails/check/{userEmail}")
    public ResultVO<?>checkEmail(@Valid@PathVariable("userEmail") String userEmail){
        return ResultVO.ok(userService.checkEmails(userEmail));
    }


    @ApiOperation("根据Id删除用户")
    @DeleteMapping("delete/{userId}")
    public ResultVO<?>deleteUserById(@Valid@PathVariable("userId") int userId){
        userService.deleteUserById(userId);
        return ResultVO.ok();
    }

    @ApiOperation("根据Id列表删除用户")
    @DeleteMapping("delete/deleteList")
    public ResultVO<?>deleteUserByList(@Valid@RequestBody List<Integer> userIdList){
        userService.deleteUserByList(userIdList);
        return ResultVO.ok();
    }

    @ApiOperation("检查用户是否存在")
    @GetMapping("check/{userId}")
    public ResultVO<?>checkUserExist(@Valid@PathVariable("userId")int userId){

        return ResultVO.ok(userService.checkUserIsExist(userId));
    }

    @ApiOperation("关注或取消关注")
    @PostMapping("follow")
    public ResultVO<?>followUser(@RequestBody FollowVO followVO){
        userService.followUser(followVO);
        return ResultVO.ok();
    }

    @ApiOperation("检查是否关注")
    @GetMapping("follow/{userId}/{fansId}")
    public ResultVO<?>checkFollow(@Valid @PathVariable("userId") int userId,@PathVariable("fansId") int fansId){
        return ResultVO.ok(userService.checkFollow(userId,fansId));
    }

    @ApiOperation("查看粉丝列表")
    @GetMapping("fans")
    public ResultVO<?>getFansList(){
        return ResultVO.ok(userService.getFansList());
    }

    @ApiOperation("查看关注列表")
    @GetMapping("follows")
    public ResultVO<?>getFollowsList(){
        return ResultVO.ok(userService.getFollowList());
    }

    @ApiOperation("获得用户列表")
    @GetMapping("users")
    public ResultVO<?>getUserList(ConditionVO conditionVO){


        return ResultVO.ok(userService.getAllUserInfo(conditionVO));
    }



    @ApiOperation("退出账户")
    @PostMapping("logout")
    public ResultVO<?>logoutUser(){
        return ResultVO.ok(userService.logoutUser());
    }


    @ApiOperation("忘记密码")
    @PostMapping("authors")
    public ResultVO<?>forgetPasswords(@RequestBody Map<String,String>map){
        userService.forgetPasswords(map.get("password"),map.get("code"));
        return ResultVO.ok();
    }

}

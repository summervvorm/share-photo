package com.example.photos.mapper;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Auther: Arrow
 * @Date: {MONTH} - {TIME}
 * @Description: com.example.photos.dao
 * @version: 1.0
 */
@Mapper
public interface LoginMapper {
    /**
     * 根据用户名和密码查询对应的用户
     */
    JSONObject checkUser(int user_id, String user_password);
//      User checkUser(int user_id,String user_password);
//    User getUserInfo(String username);
//
//    Set<String> getAllMenu();
//
//    Set<String> getAllPermissionCode();
}

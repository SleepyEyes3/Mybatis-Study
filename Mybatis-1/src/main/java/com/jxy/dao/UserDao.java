package com.jxy.dao;

import com.jxy.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserDao {
    List<User> getUserList();

    //根据id查询用户
    User getUserById(int id);

    //模糊查询
    List<User> getUserByLike(String str);

    //添加用户
    Integer insertUser(User user);

    //使用Map参数的形式传入参数
    int insertUserByMap(Map<String,Object> map);

    //更新用户
    Integer updateUser(User user);

    //删除用户
    Integer deleteUserById(int id);
}

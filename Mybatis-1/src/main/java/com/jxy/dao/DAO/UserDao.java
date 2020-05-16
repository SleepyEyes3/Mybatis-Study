package com.jxy.dao.DAO;

import com.jxy.dao.pojo.User;

import java.net.UnknownServiceException;
import java.util.List;

public interface UserDao {
    List<User> getUserList();

    //根据id查询用户
    User getUserById(int id);

    //添加用户
    Integer insertUser(User user);

    //更新用户
    Integer updateUser(User user);

    //删除用户
    Integer deleteUserById(int id);
}

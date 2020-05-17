package com.jxy.dao;

import com.jxy.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserDao {
    // 使用resultMap解决字段名与属性名不相同的问题
    List<User> getUserListByResultMap(int id);

    List<User> getUserLimit(Map<String,Integer> map);
}

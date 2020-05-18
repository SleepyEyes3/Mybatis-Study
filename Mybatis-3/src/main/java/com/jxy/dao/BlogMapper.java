package com.jxy.dao;

import com.jxy.pojo.Blog;

import java.util.List;
import java.util.Map;

@SuppressWarnings("all")
public interface BlogMapper {
    // 插入一条数据
    int addBlog(Blog blog);

    // if查询
    List<Blog> queryBlogIf(Map map);

    // set更新
    int updateBlog(Map map);

    // choose条件选择
    List<Blog> queryBlogChoose(Map map);

    // foreach查询1
    List<Blog> queryBlogForeach1(Map map);

    // foreach查询2
    List<Blog> queryBlogForeach2(List<Integer> list);
}

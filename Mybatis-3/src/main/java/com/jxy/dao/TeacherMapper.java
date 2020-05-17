package com.jxy.dao;

import com.jxy.pojo.Teacher;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface TeacherMapper {
    @Select("select * from Mybatis.teacher where #{tid}")
    Teacher getTeacherById(@Param("tid") int id);
}

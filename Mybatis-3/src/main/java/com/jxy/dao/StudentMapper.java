package com.jxy.dao;

import com.jxy.pojo.Student;
import com.jxy.pojo.Teacher;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface StudentMapper {
    List<Student> getStudentList();

    List<Student> getStudentList1();

    List<Student> getStudentList2();
}

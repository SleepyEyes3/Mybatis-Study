package com.jxy.pojo;

import lombok.Data;

@Data
public class Student {
    private Integer id;
    private String name;
    private Integer tid;

    private Teacher teacher;
}


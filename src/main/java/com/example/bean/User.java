package com.example.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String username;
    private String password;
    private String empNo;
    private String name;
    private int age;
    private String sex;
    private String deptment;
    private int ageMin;
    private int ageMax;
}

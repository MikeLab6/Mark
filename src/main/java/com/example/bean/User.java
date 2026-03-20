package com.example.bean;

import lombok.Data;

/**
 * 应用模块名称:
 * 代码描述:
 * copyright: YUNDASYS ALL RIGHTS RESERVED
 * company: YUNDA
 *
 * @author: 杨洪飞
 * @date: 2021/08/09 12:53:37
 */
@Data
public class User {
    String id;
    String empNo;
    String name;
    int age;
    String sex;
    String deptment;
    int ageMin;
    int ageMax;
}

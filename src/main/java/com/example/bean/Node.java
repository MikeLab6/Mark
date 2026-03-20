package com.example.bean;

import lombok.Data;

import java.util.List;

/**
 * 应用模块名称:
 * 代码描述:
 *
 * @author: 杨洪飞
 * @date: 2025/03/17 15:32:17
 */
@Data
public class Node {
    Integer id;
    List<Node> children;
}

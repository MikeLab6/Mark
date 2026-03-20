package com.example.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LoginLog {
    public static final int STATUS_SUCCESS = 1;
    public static final int STATUS_FAIL = 0;
    private String id;
    private String username;
    private String ip;
    private Integer status;
    private String message;
    private LocalDateTime loginTime;
}

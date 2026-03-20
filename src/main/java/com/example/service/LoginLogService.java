package com.example.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.example.entity.LoginLog;
import com.example.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Service
public class LoginLogService {

    @Autowired
    private RedisUtil redisUtil;

    public void recordLoginLog(String username, Integer status, String message, HttpServletRequest request) {
        LoginLog loginLog = new LoginLog();
        loginLog.setId(IdUtil.fastSimpleUUID());
        loginLog.setUsername(username);
        loginLog.setIp(getIpAddress(request));
        loginLog.setStatus(status);
        loginLog.setMessage(message);
        loginLog.setLoginTime(LocalDateTime.now());

        String logJson = JSONUtil.toJsonStr(loginLog);
        redisUtil.saveLoginLog(loginLog.getId(), logJson, 30);
    }

    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}

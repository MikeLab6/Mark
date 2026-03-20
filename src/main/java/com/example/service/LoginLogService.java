package com.example.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class LoginLogService {

    private static final String LOGIN_LOG_KEY_PREFIX = "login:log:";
    private static final String LOGIN_LOG_LIST_KEY = "login:log:list";
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void recordLoginLog(String username, Integer status, String message, String ip) {
        String logId = UUID.randomUUID().toString().replace("-", "");
        String key = LOGIN_LOG_KEY_PREFIX + logId;

        LocalDateTime now = LocalDateTime.now();
        String timeStr = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        String logContent = String.format("用户[%s]于[%s]登录，状态[%s]，IP[%s]，信息[%s]",
                username, timeStr, status == 1 ? "成功" : "失败", ip, message);

        redisTemplate.opsForValue().set(key, logContent, 30, TimeUnit.DAYS);
        redisTemplate.opsForList().leftPush(LOGIN_LOG_LIST_KEY, logContent);

        log.info(logContent);
    }
}

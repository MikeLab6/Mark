package com.example.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class LoginLogServiceTest {

    @Autowired
    private LoginLogService loginLogService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void testRecordLoginLogSuccess() {
        String username = "logtestuser";
        int status = 1;
        String message = "登录成功";
        String ip = "127.0.0.1";

        loginLogService.recordLoginLog(username, status, message, ip);

        List<Object> logs = redisTemplate.opsForList().range("login:log:list", 0, 0);
        assertNotNull(logs);
        assertFalse(logs.isEmpty());

        String logContent = (String) logs.get(0);
        assertTrue(logContent.contains(username));
        assertTrue(logContent.contains("成功"));
        assertTrue(logContent.contains(ip));
        assertTrue(logContent.contains(message));
    }

    @Test
    public void testRecordLoginLogFailure() {
        String username = "logtestuser2";
        int status = 0;
        String message = "密码错误";
        String ip = "192.168.1.1";

        loginLogService.recordLoginLog(username, status, message, ip);

        List<Object> logs = redisTemplate.opsForList().range("login:log:list", 0, 0);
        assertNotNull(logs);
        assertFalse(logs.isEmpty());

        String logContent = (String) logs.get(0);
        assertTrue(logContent.contains(username));
        assertTrue(logContent.contains("失败"));
        assertTrue(logContent.contains(ip));
        assertTrue(logContent.contains(message));
    }

    @Test
    public void testRecordLoginLogWithNullIp() {
        String username = "logtestuser3";
        int status = 1;
        String message = "登录成功";
        String ip = null;

        loginLogService.recordLoginLog(username, status, message, ip);

        List<Object> logs = redisTemplate.opsForList().range("login:log:list", 0, 0);
        assertNotNull(logs);
        assertFalse(logs.isEmpty());
    }

    @Test
    public void testRecordLoginLogWithEmptyIp() {
        String username = "logtestuser4";
        int status = 0;
        String message = "用户不存在";
        String ip = "";

        loginLogService.recordLoginLog(username, status, message, ip);

        List<Object> logs = redisTemplate.opsForList().range("login:log:list", 0, 0);
        assertNotNull(logs);
        assertFalse(logs.isEmpty());
    }
}

package com.example.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    private static final String USER_PASSWORD_PREFIX = "user:password:";
    private static final String LOGIN_LOG_PREFIX = "login:log:";
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public void setUserPassword(String username, String password) {
        stringRedisTemplate.opsForValue().set(USER_PASSWORD_PREFIX + username, password);
    }

    public String getUserPassword(String username) {
        return stringRedisTemplate.opsForValue().get(USER_PASSWORD_PREFIX + username);
    }

    public void saveLoginLog(String logId, String logJson, long expireDays) {
        stringRedisTemplate.opsForValue().set(LOGIN_LOG_PREFIX + logId, logJson, expireDays, TimeUnit.DAYS);
    }

    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    public void setWithExpire(String key, String value, long timeout, TimeUnit unit) {
        stringRedisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    public boolean delete(String key) {
        return Boolean.TRUE.equals(stringRedisTemplate.delete(key));
    }

    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(key));
    }
}

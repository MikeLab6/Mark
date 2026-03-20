package com.example.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RedisUtilTest {

    @Mock
    private StringRedisTemplate stringRedisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private RedisUtil redisUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void testSetUserPassword() {
        String username = "admin";
        String password = "e10adc3949ba59abbe56e057f20f883e";

        redisUtil.setUserPassword(username, password);

        verify(valueOperations, times(1)).set("user:password:admin", password);
    }

    @Test
    void testGetUserPassword() {
        String username = "admin";
        String expectedPassword = "e10adc3949ba59abbe56e057f20f883e";

        when(valueOperations.get("user:password:admin")).thenReturn(expectedPassword);

        String actualPassword = redisUtil.getUserPassword(username);

        assertEquals(expectedPassword, actualPassword);
        verify(valueOperations, times(1)).get("user:password:admin");
    }

    @Test
    void testGetUserPasswordNotFound() {
        String username = "nonexistent";

        when(valueOperations.get("user:password:nonexistent")).thenReturn(null);

        String actualPassword = redisUtil.getUserPassword(username);

        assertNull(actualPassword);
    }

    @Test
    void testSaveLoginLog() {
        String logId = "test-log-id";
        String logJson = "{\"username\":\"admin\"}";
        long expireDays = 30;

        redisUtil.saveLoginLog(logId, logJson, expireDays);

        verify(valueOperations, times(1)).set("login:log:test-log-id", logJson, expireDays, TimeUnit.DAYS);
    }

    @Test
    void testSet() {
        String key = "testKey";
        String value = "testValue";

        redisUtil.set(key, value);

        verify(valueOperations, times(1)).set(key, value);
    }

    @Test
    void testGet() {
        String key = "testKey";
        String expectedValue = "testValue";

        when(valueOperations.get(key)).thenReturn(expectedValue);

        String actualValue = redisUtil.get(key);

        assertEquals(expectedValue, actualValue);
    }

    @Test
    void testSetWithExpire() {
        String key = "testKey";
        String value = "testValue";
        long timeout = 60;
        TimeUnit unit = TimeUnit.SECONDS;

        redisUtil.setWithExpire(key, value, timeout, unit);

        verify(valueOperations, times(1)).set(key, value, timeout, unit);
    }

    @Test
    void testDeleteSuccess() {
        String key = "testKey";

        when(stringRedisTemplate.delete(key)).thenReturn(true);

        boolean result = redisUtil.delete(key);

        assertTrue(result);
    }

    @Test
    void testDeleteFail() {
        String key = "nonexistentKey";

        when(stringRedisTemplate.delete(key)).thenReturn(false);

        boolean result = redisUtil.delete(key);

        assertFalse(result);
    }

    @Test
    void testHasKeyTrue() {
        String key = "testKey";

        when(stringRedisTemplate.hasKey(key)).thenReturn(true);

        boolean result = redisUtil.hasKey(key);

        assertTrue(result);
    }

    @Test
    void testHasKeyFalse() {
        String key = "nonexistentKey";

        when(stringRedisTemplate.hasKey(key)).thenReturn(false);

        boolean result = redisUtil.hasKey(key);

        assertFalse(result);
    }
}

package com.example.service;

import com.example.bean.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {

    private static final String TEST_USERNAME = "servicetest";
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @BeforeEach
    public void setUp() {
        redisTemplate.delete("user:" + TEST_USERNAME);
    }

    @Test
    public void testSaveAndGetUser() {
        User user = new User();
        user.setId("1");
        user.setUsername(TEST_USERNAME);
        user.setPassword("password123");
        user.setName("测试用户");
        user.setAge(25);
        user.setSex("男");
        user.setDeptment("技术部");

        userService.saveUser(user);

        User retrievedUser = userService.getUserByUsername(TEST_USERNAME);

        assertNotNull(retrievedUser);
        assertEquals(TEST_USERNAME, retrievedUser.getUsername());
        assertEquals("测试用户", retrievedUser.getName());
        assertEquals("password123", retrievedUser.getPassword());
        assertEquals(25, retrievedUser.getAge());
    }

    @Test
    public void testGetUserNotFound() {
        User user = userService.getUserByUsername("nonexistentuser");
        assertNull(user);
    }

    @Test
    public void testUpdateUser() {
        User user = new User();
        user.setId("1");
        user.setUsername(TEST_USERNAME);
        user.setPassword("oldpassword");
        user.setName("旧名字");
        userService.saveUser(user);

        User updatedUser = new User();
        updatedUser.setId("1");
        updatedUser.setUsername(TEST_USERNAME);
        updatedUser.setPassword("newpassword");
        updatedUser.setName("新名字");
        userService.saveUser(updatedUser);

        User retrievedUser = userService.getUserByUsername(TEST_USERNAME);

        assertNotNull(retrievedUser);
        assertEquals("新名字", retrievedUser.getName());
        assertEquals("newpassword", retrievedUser.getPassword());
    }
}

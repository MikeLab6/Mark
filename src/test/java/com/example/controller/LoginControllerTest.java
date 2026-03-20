package com.example.controller;

import cn.hutool.crypto.digest.DigestUtil;
import com.example.bean.User;
import com.example.common.Result;
import com.example.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerTest {

    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_PASSWORD = "123456";
    private static final String TEST_NAME = "测试用户";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        // 清理测试数据
        redisTemplate.delete("user:" + TEST_USERNAME);
        redisTemplate.delete("user:wronguser");

        // 创建测试用户
        User user = new User();
        user.setId("1");
        user.setUsername(TEST_USERNAME);
        user.setPassword(DigestUtil.md5Hex(TEST_PASSWORD));
        user.setName(TEST_NAME);
        user.setAge(25);
        user.setSex("男");
        user.setDeptment("技术部");

        userService.saveUser(user);
    }

    @Test
    public void testLoginSuccess() throws Exception {
        MvcResult result = mockMvc.perform(post("/login")
                .param("username", TEST_USERNAME)
                .param("password", TEST_PASSWORD)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        Result<?> response = objectMapper.readValue(content, Result.class);

        assertEquals(200, response.getCode());
        assertEquals("登录成功", response.getMessage());
        assertNotNull(response.getData());
    }

    @Test
    public void testLoginUserNotFound() throws Exception {
        MvcResult result = mockMvc.perform(post("/login")
                .param("username", "nonexistent")
                .param("password", "123456")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        Result<?> response = objectMapper.readValue(content, Result.class);

        assertEquals(401, response.getCode());
        assertEquals("用户不存在", response.getMessage());
    }

    @Test
    public void testLoginWrongPassword() throws Exception {
        MvcResult result = mockMvc.perform(post("/login")
                .param("username", TEST_USERNAME)
                .param("password", "wrongpassword")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        Result<?> response = objectMapper.readValue(content, Result.class);

        assertEquals(401, response.getCode());
        assertEquals("密码错误", response.getMessage());
    }

    @Test
    public void testLoginEmptyUsername() throws Exception {
        MvcResult result = mockMvc.perform(post("/login")
                .param("username", "")
                .param("password", TEST_PASSWORD)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        Result<?> response = objectMapper.readValue(content, Result.class);

        assertEquals(401, response.getCode());
    }

    @Test
    public void testLoginEmptyPassword() throws Exception {
        MvcResult result = mockMvc.perform(post("/login")
                .param("username", TEST_USERNAME)
                .param("password", "")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        Result<?> response = objectMapper.readValue(content, Result.class);

        assertEquals(401, response.getCode());
    }

    @Test
    public void testUnauthorizedAccess() throws Exception {
        MvcResult result = mockMvc.perform(get("/currentUser")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        Result<?> response = objectMapper.readValue(content, Result.class);

        assertEquals(401, response.getCode());
        assertEquals("未登录", response.getMessage());
    }

    @Test
    public void testLogoutWithoutLogin() throws Exception {
        MvcResult result = mockMvc.perform(post("/logout")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        Result<?> response = objectMapper.readValue(content, Result.class);

        assertEquals(200, response.getCode());
    }
}

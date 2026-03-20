package com.example.controller;

import com.example.util.MD5Util;
import com.example.util.RedisUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RedisUtil redisUtil;

    private String testUsername;
    private String testPassword;
    private String testPasswordMd5;

    @BeforeEach
    void setUp() {
        testUsername = "admin";
        testPassword = "123456";
        testPasswordMd5 = MD5Util.encrypt(testPassword);
    }

    @Test
    void testLoginSuccess() throws Exception {
        when(redisUtil.getUserPassword(testUsername)).thenReturn(testPasswordMd5);

        String requestBody = "{\"username\":\"admin\",\"password\":\"123456\"}";

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("登录成功"))
                .andExpect(jsonPath("$.data").value("admin"));

        verify(redisUtil, times(1)).getUserPassword(testUsername);
    }

    @Test
    void testLoginUserNotFound() throws Exception {
        when(redisUtil.getUserPassword("nonexistent")).thenReturn(null);

        String requestBody = "{\"username\":\"nonexistent\",\"password\":\"123456\"}";

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(401))
                .andExpect(jsonPath("$.message").value("用户不存在"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    void testLoginWrongPassword() throws Exception {
        when(redisUtil.getUserPassword(testUsername)).thenReturn(testPasswordMd5);

        String requestBody = "{\"username\":\"admin\",\"password\":\"wrongpassword\"}";

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(401))
                .andExpect(jsonPath("$.message").value("密码错误"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    void testLoginEmptyUsername() throws Exception {
        String requestBody = "{\"username\":\"\",\"password\":\"123456\"}";

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("用户名不能为空"));
    }

    @Test
    void testLoginEmptyPassword() throws Exception {
        String requestBody = "{\"username\":\"admin\",\"password\":\"\"}";

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("密码不能为空"));
    }

    @Test
    void testLoginNullUsername() throws Exception {
        String requestBody = "{\"password\":\"123456\"}";

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("用户名不能为空"));
    }

    @Test
    void testLoginNullPassword() throws Exception {
        String requestBody = "{\"username\":\"admin\"}";

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("密码不能为空"));
    }

    @Test
    void testLogout() throws Exception {
        mockMvc.perform(post("/logout"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("退出成功"));
    }

    @Test
    void testIsLoginNotLoggedIn() throws Exception {
        mockMvc.perform(get("/isLogin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(401))
                .andExpect(jsonPath("$.message").value("未登录"));
    }
}

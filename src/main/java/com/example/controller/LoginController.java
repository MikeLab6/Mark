package com.example.controller;

import com.example.common.Result;
import com.example.dto.LoginRequest;
import com.example.entity.LoginLog;
import com.example.service.LoginLogService;
import com.example.util.MD5Util;
import com.example.util.RedisUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class LoginController {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private LoginLogService loginLogService;

    @PostMapping("/login")
    public Result<String> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        if (username == null || username.trim().isEmpty()) {
            return Result.error(400, "用户名不能为空");
        }
        if (password == null || password.trim().isEmpty()) {
            return Result.error(400, "密码不能为空");
        }

        String passwordInRedis = redisUtil.getUserPassword(username);
        if (passwordInRedis == null) {
            loginLogService.recordLoginLog(username, LoginLog.STATUS_FAIL, "用户不存在", request);
            return Result.error(401, "用户不存在");
        }

        String inputPasswordMd5 = MD5Util.encrypt(password);
        if (!inputPasswordMd5.equals(passwordInRedis)) {
            loginLogService.recordLoginLog(username, LoginLog.STATUS_FAIL, "密码错误", request);
            return Result.error(401, "密码错误");
        }

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);

        try {
            subject.login(token);
            loginLogService.recordLoginLog(username, LoginLog.STATUS_SUCCESS, "登录成功", request);
            return Result.success("登录成功", username);
        } catch (UnknownAccountException e) {
            loginLogService.recordLoginLog(username, LoginLog.STATUS_FAIL, "用户不存在", request);
            return Result.error(401, "用户不存在");
        } catch (IncorrectCredentialsException e) {
            loginLogService.recordLoginLog(username, LoginLog.STATUS_FAIL, "密码错误", request);
            return Result.error(401, "密码错误");
        } catch (LockedAccountException e) {
            loginLogService.recordLoginLog(username, LoginLog.STATUS_FAIL, "账户被锁定", request);
            return Result.error(401, "账户被锁定");
        } catch (AuthenticationException e) {
            loginLogService.recordLoginLog(username, LoginLog.STATUS_FAIL, "认证失败", request);
            return Result.error(401, "认证失败");
        }
    }

    @PostMapping("/logout")
    public Result<String> logout() {
        SecurityUtils.getSubject().logout();
        return Result.success("退出成功");
    }

    @GetMapping("/isLogin")
    public Result<String> isLogin() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return Result.success("已登录", (String) subject.getPrincipal());
        }
        return Result.error(401, "未登录");
    }
}

package com.example.controller;

import com.example.bean.User;
import com.example.common.Result;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestParam String username, @RequestParam String password) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);

        try {
            subject.login(token);
            User user = (User) subject.getPrincipal();

            Map<String, Object> data = new HashMap<>();
            data.put("username", user.getUsername());
            data.put("name", user.getName());
            data.put("sessionId", subject.getSession().getId());

            return Result.success("登录成功", data);
        } catch (UnknownAccountException e) {
            return Result.error(401, "用户不存在");
        } catch (IncorrectCredentialsException e) {
            return Result.error(401, "密码错误");
        } catch (LockedAccountException e) {
            return Result.error(401, "账号已被锁定");
        } catch (AuthenticationException e) {
            return Result.error(401, "登录失败：" + e.getMessage());
        }
    }

    @PostMapping("/logout")
    public Result<Void> logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return Result.success("退出成功", null);
    }

    @GetMapping("/unauthorized")
    public Result<Void> unauthorized() {
        return Result.error(401, "未登录或登录已过期");
    }

    @GetMapping("/currentUser")
    public Result<User> getCurrentUser() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            User user = (User) subject.getPrincipal();
            return Result.success(user);
        }
        return Result.error(401, "未登录");
    }
}

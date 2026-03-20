package com.example.shiro;

import cn.hutool.crypto.digest.DigestUtil;
import com.example.bean.User;
import com.example.service.LoginLogService;
import com.example.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Autowired
    private LoginLogService loginLogService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        String username = usernamePasswordToken.getUsername();
        String password = new String(usernamePasswordToken.getPassword());

        User user = userService.getUserByUsername(username);

        String ip = getClientIp();

        if (user == null) {
            loginLogService.recordLoginLog(username, 0, "用户不存在", ip);
            throw new UnknownAccountException("用户不存在");
        }

        String inputPasswordMd5 = DigestUtil.md5Hex(password);
        if (!inputPasswordMd5.equals(user.getPassword())) {
            loginLogService.recordLoginLog(username, 0, "密码错误", ip);
            throw new IncorrectCredentialsException("密码错误");
        }

        loginLogService.recordLoginLog(username, 1, "登录成功", ip);

        return new SimpleAuthenticationInfo(user, password, getName());
    }

    private String getClientIp() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String ip = request.getHeader("X-Forwarded-For");
                if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("Proxy-Client-IP");
                }
                if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("WL-Proxy-Client-IP");
                }
                if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getRemoteAddr();
                }
                return ip;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "unknown";
    }
}

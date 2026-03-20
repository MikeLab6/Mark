package com.example.service;

import com.example.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final String USER_KEY_PREFIX = "user:";
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public User getUserByUsername(String username) {
        String key = USER_KEY_PREFIX + username;
        Object userObj = redisTemplate.opsForValue().get(key);
        if (userObj instanceof User) {
            return (User) userObj;
        }
        return null;
    }

    public void saveUser(User user) {
        String key = USER_KEY_PREFIX + user.getUsername();
        redisTemplate.opsForValue().set(key, user);
    }
}

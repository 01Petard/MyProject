package com.hzx.controller;

import com.hzx.config.CacheConfig;
import com.hzx.util.JwtUtil;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(CacheConfig.class);

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Data
    static public class User {
        private Long userId;
        private String username;
        private String password;

    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
//    public String login(@RequestBody User user) {
//        String username = user.getUsername();
//        String password = user.getPassword();
        if (!("admin".equals(username) && "123".equals(password)) ){
            throw new RuntimeException("用户名或密码错误");
        }

        Long userId = 666L;
        String token = jwtUtil.generateToken(userId, username);
        logger.info(token);

        // 将 token 存储到 Redis 中，key 为 "token:<username>"
        redisTemplate.opsForValue().set("token:" + userId, token, 10, TimeUnit.MINUTES);
        return token;
    }

    @GetMapping("/profile")
    public String profile(@RequestHeader("Authorization") String token) {
        if (!jwtUtil.validateToken(token)) {
            throw new RuntimeException("token验证失败");
        }

        Long userId = jwtUtil.getUserIdFromToken(token);
        String username = jwtUtil.getUsernameFromToken(token);

        // 从 Redis 中读取 token 进行验证
        String storedToken = (String) redisTemplate.opsForValue().get("token:" + userId);
        if (storedToken == null || !storedToken.equals(token)) {
            throw new RuntimeException("Token mismatch");
        }
        logger.info("Profile of user: {} with ID: {}", username, userId);
        return "Profile of user: " + username + " with ID: " + userId;
    }

    @DeleteMapping("/logout")
    public boolean logout(@RequestParam Long userId) {
        try {
            redisTemplate.delete("token:" + userId.toString());
            logger.info("用户：{} 登出成功！", userId);
            return true;
        }catch (Exception e){
            logger.info("用户：{} 登出失败！", userId);
            return false;
        }
    }
}

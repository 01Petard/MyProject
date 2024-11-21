package com.hzx.controller;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
public class RedisTestController {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/test-redis")
    public String testRedis() {
        // 测试 RedisTemplate
        redisTemplate.opsForValue().set("testKey", "testValue");
        Object value = redisTemplate.opsForValue().get("testKey");
        System.out.println("RedisTemplate Value: " + value);

        // 测试 StringRedisTemplate
        stringRedisTemplate.opsForValue().set("stringTestKey", "stringTestValue");
        String stringValue = stringRedisTemplate.opsForValue().get("stringTestKey");
        System.out.println("StringRedisTemplate Value: " + stringValue);

        return "Redis test successful";
    }
}
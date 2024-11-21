package com.hzx.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@Slf4j
public class RedisConfiguration {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        log.info("开始创建redis模板对象...");

        // 创建 RedisTemplate
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        // 设置连接工厂
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 设置序列化工具
        // 设置 Redis key 的序列化器
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        // 设置 Redis value 的序列化器
        GenericJackson2JsonRedisSerializer jsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        redisTemplate.setValueSerializer(jsonRedisSerializer);

        // 设置 Hash key 的序列化器
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        // 设置 Hash value 的序列化器
        redisTemplate.setHashValueSerializer(jsonRedisSerializer);

        // 初始化 RedisTemplate
        redisTemplate.afterPropertiesSet();

        log.info("完成创建redis模板对象：{}", redisTemplate);
        return redisTemplate;
    }
}
package com.hzx.service.impl;

import com.hzx.common.constant.MessageConstant;
import com.hzx.common.constant.RedisKeyConstant;
import com.hzx.common.exception.RedisErrorException;
import com.hzx.service.MessageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Transactional
public class MessageServiceImpl implements MessageService {


    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public void sendRegisterMessage(String phone) {
        redisTemplate.opsForValue().set(RedisKeyConstant.USER_REGISTER + ":" + phone, getMessageCode(), 10, TimeUnit.MINUTES);

    }

    @Override
    public void sendKeyMessage(String phone, String key) {
        //如果key不是空的，按照key来发送验证码；如果是空的，抛出异常
        if (StringUtils.isBlank(key)) {
            throw new RedisErrorException(MessageConstant.REDIS_BLANK_KEY);
        }
        redisTemplate.opsForValue().set(key, getMessageCode(), 10, TimeUnit.MINUTES);
    }

    @Override
    public void deleteKeyMessage(String phone, String key) {
        //如果key不是空的，按照key删除数据；如果是空的，抛出异常
        if (StringUtils.isBlank(key)) {
            throw new RedisErrorException(MessageConstant.REDIS_BLANK_KEY);
        }
        redisTemplate.delete(key);
    }

    private String getMessageCode() {
        // 生成一个随机数
        String randomNumber = IntStream.generate(() -> new Random().nextInt(10))
                .limit(6)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining());

        System.out.println("验证码：" + randomNumber);
        return randomNumber;
    }

}

package com.hzx.controller.v2;


import com.hzx.common.result.Result;
import com.hzx.common.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@CrossOrigin
@RequestMapping("/user/login")
@RestController
public class LoginController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping
    public Result login(@RequestParam(value = "authentication") String dtoToken) {

        // 去redis里查找是否有一样的token
        // 所有的 token 键都以 "token:" 作为前缀
        String tokenPrefix = "token:";

        // 获取所有的 keys
        Set<String> keys = redisTemplate.keys(tokenPrefix + "*");

        if (keys != null) {
            for (String key : keys) {
                String value = (String) redisTemplate.opsForValue().get(key);
                if (dtoToken.equals(value)) {
                    // token 验证成功，返回成功信息
                    return Result.success("token登录成功");
                }
            }
        }
        // token 验证失败，返回失败信息
        return Result.error("token已过期，请重新登录！");
    }



}

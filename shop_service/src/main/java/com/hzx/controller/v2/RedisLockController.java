package com.hzx.controller.v2;


import com.hzx.common.annotation.HzxRedisLock;
import com.hzx.common.result.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("redis/")
public class RedisLockController {

    @HzxRedisLock(name = "'xxBusinessLock-' + #user.account", waitTime = 10, leaseTime = 20)
    @GetMapping("/test")
    public Result<String> testConfig() throws InterruptedException {
        Thread.sleep(120000);
        return Result.success("RedisLock");
    }

}

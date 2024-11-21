package com.hzx.controller.v2;


import com.hzx.common.result.Result;
import com.hzx.common.utils.AliSmsUtil;
import com.hzx.common.utils.JwtUtil;
import com.hzx.model.entity.User;
import com.hzx.service.UserService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user/auth")
@Slf4j
@CrossOrigin
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AliSmsUtil aliSmsUtil;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @PostMapping("/sendCode/{phone}")
    public Result sendCode(@PathVariable(value = "phone") String phone) {
        // 1.生成验证码
        String code = aliSmsUtil.generateCode();

        // 2.发送验证码到redis
        redisTemplate.opsForValue().set("code:" + phone, code, 5, TimeUnit.MINUTES);

        // 3.调用阿里云服务，发送验证码
//        Boolean isMessageSendOK = aliSmsUtil.sendSms(phone, code);
        Boolean isMessageSendOK = true;

        // 4.返回结果
        if (isMessageSendOK){
            return Result.success("验证码已发送！");
        }else {
            return Result.error("验证码发送失败！");
        }
    }

    @PostMapping("/verifyCode/{phone}/{code}")
    public Result verifyCode(@PathVariable(value = "phone") String phone, @PathVariable(value = "code") String code) {

        Map<String, String> response = new HashMap<>();

        // 1.查询redis中，手机号的验证码
        String correctCode = (String) redisTemplate.opsForValue().get("code:" + phone);

        if (correctCode != null && correctCode.equals(code)) {
            // 2.根据手机号查询用户
            User user = userService.findByPhone(phone);
            // 2.1 用户不存在，先创建用户并返回用token
            if (user == null) {
                user = userService.createUser(phone);
            }
            // 2.2 判断token是否过期，过期则创建新的token
            String returnToken = "";
            String oldToken = (String) redisTemplate.opsForValue().get("token:" + phone);
            if (oldToken != null){
                // 2.3 token未过期，续上时间
                redisTemplate.expire("token:" + phone, 2, TimeUnit.HOURS);
                returnToken = oldToken;
            }else{
                // 2.4 token已过期，创建新的token，并缓存到redis
                returnToken = jwtUtil.createJWT_user(user.getId());
                redisTemplate.opsForValue().set("token:" + phone, returnToken, 2, TimeUnit.HOURS);
            }
            response.put("token", returnToken);
            response.put("message", "登录成功");



            // 3.业务结束，删除验证码
            redisTemplate.delete( "code:" + phone);
        } else {
            // 1.手机验证码不匹配，返回
            return Result.error("验证码错误");
        }
        // 4.返回结果
        return Result.success(response);
    }
}
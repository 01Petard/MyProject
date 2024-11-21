package com.hzx.common.aspect;

import com.hzx.common.annotation.HzxNoRepeatCommit;
import com.hzx.common.constant.JwtClaimsConstant;
import com.hzx.common.properties.JwtProperties;
import com.hzx.common.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@Aspect // 标记为切面
@Component // 注册为Spring Bean
@Slf4j
public class HzxNoRepeatCommitAspect {

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private StringRedisTemplate redisTemplate;


    // 定义切入点表达式
    @Pointcut("@annotation(com.hzx.common.annotation.HzxNoRepeatCommit)")
    public void hzxNoRepeatCommitPointcut() {
        // 这里不需要任何逻辑，只是一个占位符
    }

    // 环绕通知
    @Around("hzxNoRepeatCommitPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        HzxNoRepeatCommit annotation = signature.getMethod().getAnnotation(HzxNoRepeatCommit.class);

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String token = request.getHeader(jwtProperties.getUserTokenName());
        Claims claims = jwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);
        Long userId = Long.valueOf(claims.get(JwtClaimsConstant.USER_ID).toString());
        String key = userId + request.getRequestURI() + request.getClass() + request.getMethod() + request.getParameterMap();
        key = DigestUtils.md5DigestAsHex(key.getBytes(StandardCharsets.UTF_8));

        if (Boolean.FALSE.equals(redisTemplate.hasKey(key))) {
            redisTemplate.opsForValue().setIfAbsent(key, "", annotation.lockTime(), TimeUnit.SECONDS);

            try {
                return joinPoint.proceed();
            } catch (Throwable throwable) {
                redisTemplate.delete(key);
                log.error("处理异常，请重试！");
                return throwable;
            }
        } else {
            log.error("请勿重复提交！");
            return "请勿重复提交！";
        }
    }


}

package com.hzx.interceptor;

import com.hzx.common.constant.JwtClaimsConstant;
import com.hzx.common.properties.JwtProperties;
import com.hzx.common.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RepeatSubmitInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private StringRedisTemplate redisTemplate;

    public RepeatSubmitInterceptor(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader(jwtProperties.getUserTokenName());
        Claims claims = jwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);
        Long userId = Long.valueOf(claims.get(JwtClaimsConstant.USER_ID).toString());
        String key = userId + request.getRequestURI() + request.getClass() + request.getMethod() + request.getParameterMap();
        if (Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key, "1", 5, TimeUnit.SECONDS))) {
            return true; // 允许请求继续
        } else {
            // 拒绝重复请求
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            return false;
        }
    }
}
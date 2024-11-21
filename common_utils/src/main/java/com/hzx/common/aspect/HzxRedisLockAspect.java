package com.hzx.common.aspect;

import com.hzx.common.annotation.HzxRedisLock;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.TypedValue;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Aspect // 标记为切面
@Component // 注册为Spring Bean
@Slf4j
public class HzxRedisLockAspect {
    private static final ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    private static final ExpressionParser parser = new SpelExpressionParser();

    @Resource
    private RedissonClient redissonClient;

    @Around("@annotation(redisLock)")
    public Object around(ProceedingJoinPoint joinPoint, HzxRedisLock redisLock) throws Throwable {
        log.info("进入分布式锁");
        String lockName = this.getLockName(joinPoint, redisLock);
        RLock lock = redissonClient.getLock(lockName);
        boolean isLocked = false;
        try {
            isLocked = lock.tryLock(redisLock.waitTime(), redisLock.leaseTime(), TimeUnit.SECONDS);
            if (!isLocked) {
                throw new RuntimeException("获取分布式锁失败！");
            }
            // 返回方法
            return joinPoint.proceed();
        } finally {
            if (isLocked && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    private String getLockName(ProceedingJoinPoint joinPoint, HzxRedisLock redisLock) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = resolveMethod(signature, joinPoint.getTarget());
        EvaluationContext context = new MethodBasedEvaluationContext(
                TypedValue.NULL,
                method,
                joinPoint.getArgs(),
                parameterNameDiscoverer);
        Expression expression = parser.parseExpression(redisLock.name());
        return expression.getValue(context, String.class);
    }

    private Method resolveMethod(MethodSignature signature, Object target) {
        Class<?> targetClass = target.getClass();
        try {
            return targetClass.getMethod(signature.getName(), signature.getMethod().getParameterTypes());
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("无法处理目标方法" + signature.getName(), e);
        }
    }

}

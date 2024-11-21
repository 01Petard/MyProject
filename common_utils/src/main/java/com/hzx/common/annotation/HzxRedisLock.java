package com.hzx.common.annotation;


import lombok.Data;

import java.lang.annotation.*;

@Documented
@Inherited
@Target(ElementType.METHOD) // 表示该注解只能用于方法级别
@Retention(RetentionPolicy.RUNTIME) // 运行时保留，这样才能在运行时通过反射读取
public @interface HzxRedisLock {

    // 锁名称
    String name() default "";

    // 锁等待时间
    long waitTime() default 5;

    // 锁超时释放时间（默认-1：会出发自动续期）
    long leaseTime() default -1;



}

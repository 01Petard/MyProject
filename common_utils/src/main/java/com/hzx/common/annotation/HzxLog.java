package com.hzx.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) // 表示该注解只能用于方法级别
@Retention(RetentionPolicy.RUNTIME) // 运行时保留，这样才能在运行时通过反射读取
public @interface HzxLog {
    String value() default "执行@HzxLog"; // 可以添加一些描述信息，默认为空
}
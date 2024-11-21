package com.hzx.common.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HzxNoRepeatCommit {

    long lockTime() default 5;

}

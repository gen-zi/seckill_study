package com.example.seckill.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lgy
 * @Description
 * @ClassName AccessLimit
 * @data 2021/9/24
 * @Version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AccessLimit {
    //多少秒内
    public int seconds();
    //最大访问次数
    public int maxCount();
    //是否需要登录
    public boolean needLogin() default true;
}

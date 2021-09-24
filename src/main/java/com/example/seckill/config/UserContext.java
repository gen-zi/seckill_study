package com.example.seckill.config;

import com.example.seckill.pojo.User;

/**
 * @author lgy
 * @Description
 * @ClassName UserContext
 * @data 2021/9/24
 * @Version 1.0
 */
public class UserContext {

    private static final ThreadLocal<User> userThreadLocal = new ThreadLocal<>();

    public static void setUser(User user) {
        userThreadLocal.set(user);
    }

    public static User getUser(){
        return userThreadLocal.get();
    }
}

package com.example.seckill.utils;

import java.util.UUID;

/**
 * @author lgy
 * @Description UUID工具类，得到UUID作为Cookie
 * @ClassName UUIDUtil
 * @data 2021/9/15
 * @Version 1.0
 */
public class UUIDUtil {
    /**
     * UUID得到唯一标识
     * @return
     */
    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }
}

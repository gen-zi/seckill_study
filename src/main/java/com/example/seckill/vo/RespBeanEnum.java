package com.example.seckill.vo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lgy
 * @Description
 * @ClassName RespBeanEnum
 * @data 2021/9/14
 * @Version 1.0
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum RespBeanEnum {
    //通用
    SUCCESS(200, "success"),
    ERROR(500, "服务端异常"),

    //登录功能 5002xx
    SESSION_ERROR(500210, "session不存在或者已失效"),
    LOGIN_ERROR(500211, "登录信息有误"),
    MOBILE_ERROR(500212, "手机号码格式错误"),
    BIND_ERROR(500213, "验证错误"),

    //秒杀功能5003xx
    STOCK_ERROR(500300, "库存不足，秒杀失败"),
    REPEATED_ERROR(500301, "用户已购买过，禁止重复购买"),

    //安全5004xx
    ILLEGAL_ACCESS(500400,"非法访问，请重新尝试");

    private final int code;
    private final String message;
}

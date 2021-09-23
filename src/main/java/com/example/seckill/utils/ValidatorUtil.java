package com.example.seckill.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lgy
 * @Description 验证工具类
 * @ClassName ValidatorUtil
 * @data 2021/9/14
 * @Version 1.0
 */
public class ValidatorUtil {

    private static final Pattern MOBILE_PATTERN = Pattern.compile("^1(3\\d|4[5-8]|5[0-35-9]|6[567]|7[01345-8]|8\\d|9[025-9])\\d{8}$");

    /**
     * 验证字符串是否为合法手机号
     * @param mobile
     * @return
     */
    public static boolean isMobile(String mobile){
//        if (StringUtils.isEmpty(mobile))
//            return false;
        Matcher matcher = MOBILE_PATTERN.matcher(mobile);
        return matcher.matches();
    }
}

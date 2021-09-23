package com.example.seckill.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author lgy
 * @Description md5加盐加密工具类
 * @ClassName MD5Utils
 * @data 2021/9/14
 * @Version 1.0
 */
public class MD5Util {
    //前端传过来的盐，现在先写死
    private static final String SALT = "1a2b3c4d";

    /**
     * 封装md5加密接口
     * @param s
     * @return
     */
    public static String  md5(String s){
        return DigestUtils.md5Hex(s);
    }

    /**
     * 前端数据到后端的一次加密
     * @param password
     * @return
     */
    public static String inputPassToFormPass(String password){
        return md5("" + SALT.charAt(0) + SALT.charAt(2) + password + SALT.charAt(4) + SALT.charAt(6));
    }

    /**
     * 后端数据到数据库的一次加密
     * @param password
     * @param DBSalt
     * @return
     */
    public static String formPassToDBPass(String password, String DBSalt){
        return md5("" + DBSalt.charAt(0) + DBSalt.charAt(2) + password + DBSalt.charAt(4) + DBSalt.charAt(6));
    }

    /**
     * 封装前端到数据库的两次加密
     * @param password
     * @param DBSalt
     * @return
     */
    public static String inputPassToDBPass(String password, String DBSalt){
        return formPassToDBPass(inputPassToFormPass(password), DBSalt);
    }

    public static void main(String[] args) {
        String password = "123456";
        System.out.println(inputPassToFormPass(password));
        System.out.println("d3b1294a61a07da9b49b6e22b2cbd7f9");
        System.out.println(inputPassToDBPass(password, SALT));
    }
}

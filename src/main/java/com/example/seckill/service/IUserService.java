package com.example.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.seckill.pojo.User;
import com.example.seckill.vo.LoginBean;
import com.example.seckill.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lgy
 * @since 2021-09-12
 */
public interface IUserService extends IService<User> {

    RespBean login(HttpServletRequest request, HttpServletResponse response, LoginBean loginBean);

    User getUserByTicket(HttpServletRequest request, HttpServletResponse response, String ticket);
}

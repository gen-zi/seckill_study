package com.example.seckill.controller;

import com.example.seckill.service.IUserService;
import com.example.seckill.vo.LoginBean;
import com.example.seckill.vo.RespBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @author lgy
 * @Description
 * @ClassName LoginController
 * @data 2021/9/14
 * @Version 1.0
 */
@Controller
@RequestMapping("/login")
@Slf4j
public class LoginController {
    @Autowired
    private IUserService userService;

    @RequestMapping("/toLogin")
    public String toLogin(Model model){
        return "login";
    }

    @RequestMapping("/doLogin")
    @ResponseBody
    public RespBean doLogin(@Valid LoginBean loginBean, HttpServletRequest request, HttpServletResponse response){
        log.info(loginBean.toString());
        return userService.login(request, response, loginBean);
    }
}

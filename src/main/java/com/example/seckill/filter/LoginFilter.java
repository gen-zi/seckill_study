package com.example.seckill.filter;

import com.example.seckill.pojo.User;
import com.example.seckill.service.impl.UserServiceImpl;
import com.example.seckill.utils.Constants;
import com.example.seckill.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.thymeleaf.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author lgy
 * @Description
 * @ClassName LoginFilter
 * @data 2021/9/18
 * @Version 1.0
 */
//@WebFilter(urlPatterns = "/*")
public class LoginFilter implements Filter {
    @Autowired
    private UserServiceImpl userService;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

//       用重定向还是转发都会出现这种状况，把js、css那些请求都过滤的，因为视图解析访问login.html时，里面引用的js、css会以重定向方式申请
//       所以这里只能简陋的对那些资源放行
        String requestURI = req.getRequestURI();
        if (requestURI.endsWith("/toLogin") || requestURI.endsWith("/doLogin")
            || requestURI.endsWith(".js")||requestURI.endsWith(".css")){
            chain.doFilter(request, response);
            return;
        }

        String userTicket = CookieUtil.getCookieValue(req, Constants.USER_COOKIE);
        System.out.println(req.getContextPath());
        if (StringUtils.isEmpty(userTicket)) {
            resp.sendRedirect(req.getContextPath() + "/login/toLogin");
        } else {
            User user = userService.getUserByTicket(req, resp, userTicket);
            if (null == user) {
                resp.sendRedirect(req.getContextPath() + "/login/toLogin");
            } else {
                chain.doFilter(request, response);
            }

        }
    }

    @Override
    public void destroy() {

    }
}

package com.example.seckill.config;

import com.example.seckill.pojo.User;
import com.example.seckill.service.IUserService;
import com.example.seckill.utils.Constants;
import com.example.seckill.utils.CookieUtil;
import com.example.seckill.utils.JsonUtil;
import com.example.seckill.vo.RespBean;
import com.example.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

/**
 * @author lgy
 * @Description
 * @ClassName AccessLimitHandler
 * @data 2021/9/24
 * @Version 1.0
 */
@Component
public class AccessLimitHandler implements HandlerInterceptor {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private IUserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //处理器属于方法处理器，可通过方法处理器拿到方法的相应信息
        if (handler instanceof HandlerMethod){
            User user = getUser(request, response);
            //将这个user用TreadLocal存起来，别的地方要用就能直接拿
            UserContext.setUser(user);
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            AccessLimit accessLimit = handlerMethod.getMethodAnnotation(AccessLimit.class);

            //没有这个注解就放行
            if (accessLimit == null){
                return true;
            }

            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            String key = request.getRequestURI();
            if (accessLimit.needLogin()){
                if (user == null){
                    //通过response返回前端错误信息
                    render(response, RespBeanEnum.SUCCESS_ERROR);
                    return false;
                }
                key += ":" + user.getId();
            }
            ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
            Integer count = (Integer) valueOperations.get(key);
            if (count == null){
                valueOperations.set(key, 0, seconds, TimeUnit.SECONDS);
            }else if (count < maxCount){
                valueOperations.increment(key);
            }else {
                //通过response返回错误信息
                render(response, RespBeanEnum.FREQUENT_ACCESS_ERROR);
                return false;
            }

        }
        return true;
    }

    private void render(HttpServletResponse response, RespBeanEnum frequentAccessError) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        //输出RespBean，不是RespBeanEnum
        writer.write(JsonUtil.object2JsonStr(RespBean.error(frequentAccessError)));
        writer.flush();
        writer.close();
    }


    private User getUser(HttpServletRequest request, HttpServletResponse response) {
        String userTicket = CookieUtil.getCookieValue(request, Constants.USER_COOKIE);
        return userService.getUserByTicket(request, response, userTicket);
    }
}

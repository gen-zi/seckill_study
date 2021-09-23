package com.example.seckill.config;

import com.example.seckill.pojo.User;
import com.example.seckill.service.IUserService;
import com.example.seckill.utils.Constants;
import com.example.seckill.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lgy
 * @Description
 * @ClassName UserArgumentResolver
 * @data 2021/9/17
 * @Version 1.0
 */
@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {
//    @Autowired
//    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private IUserService userService;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class<?> parameterType = methodParameter.getParameterType();
        return parameterType == User.class;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
              NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) nativeWebRequest.getNativeRequest();
        HttpServletResponse response = (HttpServletResponse) nativeWebRequest.getNativeResponse();
        String userTicket = CookieUtil.getCookieValue(request, Constants.USER_COOKIE);
//        User user = (User) redisTemplate.opsForValue().get("user:" + userTicket);
//        if (StringUtils.isEmpty(userTicket)){
//            return null;
//        }
        return userService.getUserByTicket(request, response, userTicket);
    }
}

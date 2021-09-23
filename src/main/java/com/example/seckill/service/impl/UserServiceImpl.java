package com.example.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.seckill.exception.GlobalException;
import com.example.seckill.mapper.UserMapper;
import com.example.seckill.pojo.User;
import com.example.seckill.service.IUserService;
import com.example.seckill.utils.*;
import com.example.seckill.vo.LoginBean;
import com.example.seckill.vo.RespBean;
import com.example.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lgy
 * @since 2021-09-12
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @Override
    public RespBean login(HttpServletRequest request, HttpServletResponse response, LoginBean loginBean) {
        String mobile = loginBean.getMobile();
        String password = loginBean.getPassword();

        //判断手机号和密码是否有效,用jsr303验证后就不需要这些了
//        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password))
//            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
//        if (!ValidatorUtil.isMobile(mobile))
//            return RespBean.error(RespBeanEnum.MOBILE_ERROR);

        //数据库中的id即手机号

        User user = userMapper.selectById(mobile);
        //没有这个用户
        if (null == user){
//            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }

        //验证密码是否正确，用数据库中的salt对传来的密码进行md5加密，然后判断加密后的密码是否与数据库中的密码一致
        if (MD5Util.formPassToDBPass(password, user.getSalt()).equals(user.getPassword())) {
            String userTicket = UUIDUtil.getUUID();
//            request.getSession().setAttribute(ticket, user);
            CookieUtil.setCookie(request, response, Constants.USER_COOKIE, userTicket);
            redisTemplate.opsForValue().set("user:"+userTicket, JsonUtil.object2JsonStr(user));
            return RespBean.success(userTicket);
        }
        else {
//            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
    }

    @Override
    public User getUserByTicket(HttpServletRequest request, HttpServletResponse response, String userTicket) {
        if (StringUtils.isEmpty(userTicket)) {
            return null;
        }
        String userJson = (String) redisTemplate.opsForValue().get("user:" + userTicket);
        if(userJson == null){
            return null;
        }
        User user = JsonUtil.jsonStr2Object(userJson, User.class);
        if (user != null){
            CookieUtil.setCookie(request, response, Constants.USER_COOKIE, userTicket);
        }
        return user;
    }
}

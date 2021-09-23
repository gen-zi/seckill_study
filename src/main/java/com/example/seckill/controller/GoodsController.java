package com.example.seckill.controller;

import com.example.seckill.pojo.User;
import com.example.seckill.service.IGoodsService;
import com.example.seckill.vo.DetailVo;
import com.example.seckill.vo.GoodsVo;
import com.example.seckill.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author lgy
 * @Description
 * @ClassName goodsController
 * @data 2021/9/15
 * @Version 1.0
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {
//    这是没有使用WebMvcConfig处理参数时候的版本，就是参数传进来又要拿cookie值又要到redis里找user，如果每个路径都这么搞肯定十分麻烦
//    @Autowired
//    private UserServiceImpl userService;
//    @RequestMapping("/toList")
//    public String toList(HttpServletRequest request, HttpServletResponse response, Model model, @CookieValue(Constants.USER_COOKIE) String ticket){
//        if (StringUtils.isEmpty(ticket)){
//            return "login";
//        }
//        User user = (User) userService.getUserByTicket(request, response, ticket);
//        if (null == user){
//            return "login";
//        }else {
//            model.addAttribute("user", user);
//            return "goodsList";
//        }
//    }

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

//    这是用了WebMvcConfig的addArgumentResolvers方法后的，以后所有其他路径都可以直接拿到user参数了
    @RequestMapping(value = "/toList", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String toList(Model model, User user, HttpServletRequest request, HttpServletResponse response) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goodsList");
        if (!StringUtils.isEmpty(html)){
            return html;
        }
        //手动渲染html页面
        model.addAttribute("goodsList", goodsService.getGoodsVoList());
        model.addAttribute("user", user);
        WebContext webContext = new WebContext(request, response, request.getServletContext(),
                request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsList", webContext);
        valueOperations.set("goodsList", html, 60, TimeUnit.SECONDS);
        return html;
    }


//    用redis缓存已渲染的页面，不用每次请求都渲染，但是不能实时更新数据，只能等redis缓存过期再重新请求才能更新
//    @RequestMapping(value = "/toDetail/{goodsId}", produces = "text/html;charset=UTF-8")
//    @ResponseBody
//    public String toDetail(HttpServletRequest request, HttpServletResponse response,
//                           Model model, User user, @PathVariable long goodsId){
//        //查看redis有没有页面缓存
//        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
//        String html = (String) valueOperations.get("goodsDetail" + goodsId);
//        if (!StringUtils.isEmpty(html)){
//            return html;
//        }
//        //手动渲染
//        model.addAttribute("user", user);
//        GoodsVo goods = goodsService.getGoodsVo(goodsId);
//        model.addAttribute("goods", goods);
//        goodsService.setSecKillStatusAndRemainSeconds(goods, model);
//        WebContext webContext = new WebContext(request, response, request.getServletContext(),
//                    request.getLocale(), model.asMap());
//        html = thymeleafViewResolver.getTemplateEngine().process("goodsDetail", webContext);
//        if (!StringUtils.isEmpty(html)){
//            valueOperations.set("goodsDetail" + goodsId, html, 60, TimeUnit.SECONDS);
//        }
//        return html;
//    }
    @RequestMapping("/toDetail/{goodsId}")
    @ResponseBody
    public RespBean toDetail(HttpServletRequest request, HttpServletResponse response,
                             User user, @PathVariable long goodsId){
        GoodsVo goods = goodsService.getGoodsVo(goodsId);
        Date startDate = goods.getStartDate();
        Date endDate = goods.getEndDate();
        Date now = new Date();
        int seckillStatus = 0;
        int remainSeconds = 0;
        if (now.before(startDate)){
            seckillStatus = 0;
            remainSeconds = (int) ((startDate.getTime() - now.getTime()) / 1000);
        }else if (now.after(endDate)){
            seckillStatus = 2;
        }else {
            seckillStatus = 1;
        }
        DetailVo detailVo = new DetailVo();
        detailVo.setGoodsVo(goods);
        detailVo.setUser(user);
        detailVo.setSeckillStatus(seckillStatus);
        detailVo.setRemainSeconds(remainSeconds);
        return RespBean.success(detailVo);
    }



}

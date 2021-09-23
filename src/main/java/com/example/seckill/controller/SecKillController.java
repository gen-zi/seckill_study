package com.example.seckill.controller;

import com.example.seckill.pojo.User;
import com.example.seckill.rabbitmq.MQSender;
import com.example.seckill.rabbitmq.SeckillMessage;
import com.example.seckill.service.IGoodsService;
import com.example.seckill.service.IOrderService;
import com.example.seckill.service.ISeckillOrderService;
import com.example.seckill.utils.JsonUtil;
import com.example.seckill.vo.GoodsVo;
import com.example.seckill.vo.RespBean;
import com.example.seckill.vo.RespBeanEnum;
import com.pig4cloud.captcha.SpecCaptcha;
import com.pig4cloud.captcha.base.Captcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author lgy
 * @Description
 * @ClassName SecKillController
 * @data 2021/9/18
 * @Version 1.0
 */
@Controller
@RequestMapping("/seckill")
@Slf4j
public class SecKillController implements InitializingBean {

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private ISeckillOrderService seckillOrderService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private MQSender mqSender;

    private static final Map<Long, Boolean> emptyStockMap = new HashMap<>();

//    /**
//     * 没页面静态化前
//     * @param model
//     * @param user
//     * @param goodsId
//     * @return String
//     * @version 1.0
//     */
//    @RequestMapping(value = "/doSeckill")
//    public String doSecKill(Model model, User user, Long goodsId){
//        //判断是否登录
//        if (null == user || user.getId() == null){
//            return "login";
//        }
//        model.addAttribute("user", user);
//
//        //判断存储是否足够 1
//        GoodsVo goods = goodsService.getGoodsVo(goodsId);
//        if (goods.getStockCount() < 1){
//            model.addAttribute("errmsg", RespBeanEnum.STOCK_ERROR.getMessage());
//            return "seckillFail";
//        }
//
//        //判断是否重复购买 2
//        SeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<SeckillOrder>()
//                .eq("user_id", user.getId()).eq("goods_id", goodsId));
//        if (seckillOrder != null){
//            model.addAttribute("errmsg", RespBeanEnum.REPEATED_ERROR.getMessage());
//            return "seckillFail";
//        }
//
//        //执行秒杀(减库存，生订单)
//        Order order = orderService.seckill(user, goods);
//        model.addAttribute("goods", goods);
//        model.addAttribute("order", order);
//        return "orderDetail";
//    }

//    /**
//     * 页面静态化后，及做了判断重复买优化和修复了超卖bug
//     * @param user
//     * @param goodsId
//     * @return RespBean
//     * @version 2.0
//     */
//    @RequestMapping(value = "/doSeckill", method = RequestMethod.POST)
//    @ResponseBody
//    public RespBean doSecKill(User user, Long goodsId){
//        //判断是否登录
//        if (null == user){
//            return RespBean.error(RespBeanEnum.SESSION_ERROR);
//        }
//
//        //判断存储是否足够，有用到数据库
//        GoodsVo goods = goodsService.getGoodsVo(goodsId);
//        if (goods.getStockCount() < 1){
//            return RespBean.error(RespBeanEnum.STOCK_ERROR);
//        }
//
//        //判断是否重复购买，这样在高并发情况下还是会出现重复购买的的情况（同一个用户同时发多个请求，这时还没有订单，那那些请求就有可能过下面的判断）
//        //一种办法是在生产订单时对用户id和商品id加唯一索引，如果生产订单失败就说明了有用户重复购买
//        String seckillOrderJSON = (String) redisTemplate.opsForValue().get("seckillOrder:" + goodsId + ":" + user.getId());
//        //这里没必要转成对象
//        if (!StringUtils.isEmpty(seckillOrderJSON)){
//            return RespBean.error(RespBeanEnum.REPEATED_ERROR);
//        }
//
//        //执行秒杀(减库存，生订单)
//        Order order = orderService.seckill(user, goods);
//        return RespBean.success(order);
//    }

//    /**
//     * 相对于2.0,做了判断库存的优化，直接将库存数量放到缓存中操作，在缓存中做预减
//     * @param user
//     * @param goodsId
//     * @return RespBean
//     * @version 3.0
//     */
//    @RequestMapping(value = "/doSeckill", method = RequestMethod.POST)
//    @ResponseBody
//    public RespBean doSecKill(User user, Long goodsId){
//        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
//
//        //判断是否登录
//        if (null == user){
//            return RespBean.error(RespBeanEnum.SESSION_ERROR);
//        }
//
//        if (!emptyStockMap.get(goodsId)) {
//            //判断存储是否足够，直接用缓存判断
//            Long stock = valueOperations.decrement("seckillGoods:" + goodsId);
//            if (stock < 0) {
//                emptyStockMap.put(goodsId, true);
//                valueOperations.increment("seckillGoods:" + goodsId);
//                return RespBean.error(RespBeanEnum.STOCK_ERROR);
//            }
//        }else {
//            return RespBean.error(RespBeanEnum.STOCK_ERROR);
//        }
//
//        //判断是否重复购买，这样在高并发情况下还是会出现重复购买的的情况（同一个用户同时发多个请求，这时还没有订单，那那些请求就有可能过下面的判断）
//        //一种办法是在生产订单时对用户id和商品id加唯一索引，如果生产订单失败就说明了有用户重复购买
//        String seckillOrderJSON = (String) valueOperations.get("seckillOrder:" + goodsId + ":" + user.getId());
//        //这里没必要转成对象
//        if (!StringUtils.isEmpty(seckillOrderJSON)){
//            return RespBean.error(RespBeanEnum.REPEATED_ERROR);
//        }
//
//        //执行秒杀(减库存，生订单)
//        SeckillMessage seckillMessage = new SeckillMessage();
//        seckillMessage.setUser(user);
//        seckillMessage.setGoodsId(goodsId);
//        mqSender.send(JsonUtil.object2JsonStr(seckillMessage));
//        return RespBean.success(0);
//    }

    /**
     * 相对于3.0,增加了安全优化，将秒杀接口隐藏，通过暴露的接口间接获得秒杀接口
     * @param user
     * @param goodsId
     * @return RespBean
     * @version 4.0
     */
    @RequestMapping(value = "/{path}/doSeckill", method = RequestMethod.POST)
    @ResponseBody
    public RespBean doSecKill(@PathVariable String path, User user, Long goodsId){
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();

        //判断是否登录
        if (null == user){
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        if (!orderService.checkPath(path, user, goodsId)){
            return RespBean.error(RespBeanEnum.ILLEGAL_ACCESS_ERROR);
        }
        if (!emptyStockMap.get(goodsId)) {
            //判断存储是否足够，直接用缓存判断
            Long stock = valueOperations.decrement("seckillGoods:" + goodsId);
            if (stock < 0) {
                emptyStockMap.put(goodsId, true);
                valueOperations.increment("seckillGoods:" + goodsId);
                return RespBean.error(RespBeanEnum.STOCK_ERROR);
            }
        }else {
            return RespBean.error(RespBeanEnum.STOCK_ERROR);
        }

        //判断是否重复购买，这样在高并发情况下还是会出现重复购买的的情况（同一个用户同时发多个请求，这时还没有订单，那那些请求就有可能过下面的判断）
        //一种办法是在生产订单时对用户id和商品id加唯一索引，如果生产订单失败就说明了有用户重复购买
        String seckillOrderJSON = (String) valueOperations.get("seckillOrder:" + goodsId + ":" + user.getId());
        //这里没必要转成对象
        if (!StringUtils.isEmpty(seckillOrderJSON)){
            return RespBean.error(RespBeanEnum.REPEATED_ERROR);
        }

        //执行秒杀(减库存，生订单)
        SeckillMessage seckillMessage = new SeckillMessage();
        seckillMessage.setUser(user);
        seckillMessage.setGoodsId(goodsId);
        mqSender.send(JsonUtil.object2JsonStr(seckillMessage));
        return RespBean.success(0);
    }

    /**
     * 获取验证码
     * @param user
     * @param goodsId
     * @param response
     */
    @RequestMapping(value = "/captcha", method = RequestMethod.GET)
    public void createCaptcha(User user, @RequestParam("goodsId") Long goodsId, HttpServletResponse response) {
        // 设置请求头为输出图片类型
        response.setContentType("image/gif");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        // 三个参数分别为宽、高、位数
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 5);
        // 设置字体
        specCaptcha.setFont(new Font("Verdana", Font.PLAIN, 32));  // 有默认字体，可以不用设置
        // 设置类型，纯数字、纯字母、字母数字混合，默认数字字母混合，默认区分大小写，很难通过，用数字和大写字母组合6
        specCaptcha.setCharType(Captcha.TYPE_NUM_AND_UPPER);

        //这里有时存进redis的会将goodsId为null？
        //刷新页面就会得不到goodsId，直接点击不会
        //是因为前端的在页面加载完进行的函数中直接在里面调用了生成验证码的函数，虽然是在加载细节函数后面调用，但是那个时候就是拿不到goodsId
        //直接将生成函数移到渲染完后面调用就可以了，确保了goodsId是有值的
        redisTemplate.opsForValue()
                .set("captcha:" + user.getId()+":" + goodsId, specCaptcha.text(), 180, TimeUnit.SECONDS);

        // 输出图片流
        try {
            specCaptcha.out(response.getOutputStream());
        } catch (IOException e) {
            log.info("验证码生成失败", e.getMessage());
        }
    }

    /**
     * 验证验证码，获取秒杀路径
     * @param user
     * @param goodsId
     * @param captcha
     * @return
     */
    @RequestMapping(value = "/path", method = RequestMethod.GET)
    @ResponseBody
    public RespBean getPath(User user, Long goodsId, String captcha){
        //判断用户
        if (user == null){
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }

        //二次验证验证码
        boolean result = orderService.checkCaptcha(captcha, user, goodsId);
        if (!result){
            return RespBean.error(RespBeanEnum.CAPTCHA_ERROR);
        }
        //生成路径信息
        String path = orderService.createPath(user, goodsId);
        return RespBean.success(path);
    }



    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public RespBean getResult(User user, Long goodsId){
        if (null == user){
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        Long orderId = orderService.getResult(user, goodsId);
        return RespBean.success(orderId);
    }


    //将库存预载入缓存
    @Override
    public void afterPropertiesSet() throws Exception {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        List<GoodsVo> goodsVoList = goodsService.getGoodsVoList();
        goodsVoList.forEach((a)->{
            valueOperations.set("seckillGoods:"+ a.getId(), a.getStockCount());
            emptyStockMap.put(a.getId(),false);
        });
    }
}

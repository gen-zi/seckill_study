package com.example.seckill.rabbitmq;

import com.example.seckill.pojo.User;
import com.example.seckill.service.IGoodsService;
import com.example.seckill.service.IOrderService;
import com.example.seckill.utils.JsonUtil;
import com.example.seckill.vo.GoodsVo;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

/**
 * @author lgy
 * @Description
 * @ClassName MQRecvice
 * @data 2021/9/23
 * @Version 1.0
 */
@Service
public class MQReceive {

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @RabbitListener(queues = "seckillQueue")
    public void listener(String msg){
        SeckillMessage seckillMessage = JsonUtil.jsonStr2Object(msg, SeckillMessage.class);
        User user = seckillMessage.getUser();
        GoodsVo goodsVo = goodsService.getGoodsVo(seckillMessage.getGoodsId());

        //判断数据库的库存是否足够
        if (goodsVo.getStockCount() < 1){
            return;
        }

        //因为这个时候还没有生成订单，所以还需要判断是否重复购买，其实可以将前面的重复购买删掉的，不去掉也可以，减少点MQ的压力
        String seckilOrderJSON = (String) redisTemplate.opsForValue().get("seckillOrder:" + goodsVo.getId() + ":" + user.getId());
        if (!StringUtils.isEmpty(seckilOrderJSON)){
            return;
        }

        //生成订单
        orderService.seckill(user, goodsVo);
    }
}

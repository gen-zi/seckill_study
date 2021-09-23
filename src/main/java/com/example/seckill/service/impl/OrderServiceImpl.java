package com.example.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.seckill.mapper.OrderMapper;
import com.example.seckill.pojo.Order;
import com.example.seckill.pojo.SeckillGoods;
import com.example.seckill.pojo.SeckillOrder;
import com.example.seckill.pojo.User;
import com.example.seckill.service.IGoodsService;
import com.example.seckill.service.IOrderService;
import com.example.seckill.service.ISeckillGoodsService;
import com.example.seckill.service.ISeckillOrderService;
import com.example.seckill.utils.JsonUtil;
import com.example.seckill.vo.GoodsVo;
import com.example.seckill.vo.OrderDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lgy
 * @since 2021-09-17
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private ISeckillOrderService seckillOrderService;
    @Autowired
    private ISeckillGoodsService seckillGoodsService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
//    这种方式会发生超卖的
//    @Override
//    @Transactional
//    public Order seckill(User user, GoodsVo goodsVo) {
//        //减库存
//        SeckillGoods seckillgoods = seckillGoodsService.getOne(new QueryWrapper<SeckillGoods>().eq("goods_id", goodsVo.getId()));
//        seckillgoods.setStockCount(seckillgoods.getStockCount() - 1);
//        seckillGoodsService.updateById(seckillgoods);
//
//        //生成订单
//        Order order = new Order();
//        order.setGoodsId(goodsVo.getId());
//        order.setUserId(user.getId());
//        order.setDeliveryAddrId(0L);
//        order.setGoodsName(goodsVo.getGoodsName());
//        order.setGoodsCount(1);
//        order.setGoodsPrice(goodsVo.getGoodsPrice());
//        order.setOrderChannel(1);
//        order.setStatus(0);
//        order.setCreateDate(new Date());
//        orderMapper.insert(order);
//
//        //秒杀订单，依赖订单id
//        SeckillOrder seckillOrder = new SeckillOrder();
//        seckillOrder.setOrderId(order.getId());
//        seckillOrder.setUserId(user.getId());
//        seckillOrder.setGoodsId(goodsVo.getId());
//        seckillOrderMapper.insert(seckillOrder);
//        return order;
//    }

    @Override
    @Transactional
    public Order seckill(User user, GoodsVo goodsVo) {
        //减库存
        SeckillGoods seckillgoods = seckillGoodsService.getOne(new QueryWrapper<SeckillGoods>().eq("goods_id", goodsVo.getId()));
        seckillgoods.setStockCount(seckillgoods.getStockCount() - 1);

        if (seckillgoods.getStockCount() < 1){
            redisTemplate.opsForValue().set("emptyStock:"+ goodsVo.getId(), 0);
            return null;
        }
        boolean goodsResult = seckillGoodsService.update(new UpdateWrapper<SeckillGoods>().set("stock_count",seckillgoods.getStockCount())
                .eq("goods_id", goodsVo.getId()).gt("stock_count", 0));
//        if (!goodsResult){
//            throw new GlobalException(RespBeanEnum.STOCK_ERROR);
//        }

        //生成订单
        Order order = new Order();
        order.setGoodsId(goodsVo.getId());
        order.setUserId(user.getId());
        order.setDeliveryAddrId(0L);
        order.setGoodsName(goodsVo.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(goodsVo.getGoodsPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateDate(new Date());
        //5
        orderMapper.insert(order);

        //秒杀订单，依赖订单id
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setOrderId(order.getId());
        seckillOrder.setUserId(user.getId());
        seckillOrder.setGoodsId(goodsVo.getId());
        //这里如果有重复会抛出DuplicateKeyException异常，我这里就用全局异常处理处理了
        seckillOrderService.save(seckillOrder);
        redisTemplate.opsForValue().set("seckillOrder:"+goodsVo.getId()+":"+user.getId(),
                JsonUtil.object2JsonStr(seckillOrder));
        return order;
    }

    @Override
    public OrderDetail getOrderDetailById(Long orderId) {
        Order order = orderMapper.selectById(orderId);
        GoodsVo goodsVo = goodsService.getGoodsVo(order.getGoodsId());
        OrderDetail detail = new OrderDetail();
        detail.setGoodsVo(goodsVo);
        detail.setOrder(order);
        return detail;
    }

    @Override
    public Long getResult(User user, Long goodsId) {
        SeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<SeckillOrder>()
                .eq("user_id", user.getId()).eq("goods_id", goodsId));
        if (seckillOrder != null){
            return seckillOrder.getOrderId();
        }
        if (redisTemplate.hasKey("emptyStock:" + goodsId)){
            return -1L;
        }else {
            return 0L;
        }
    }


}

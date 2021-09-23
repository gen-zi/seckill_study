package com.example.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.seckill.pojo.Order;
import com.example.seckill.pojo.User;
import com.example.seckill.vo.GoodsVo;
import com.example.seckill.vo.OrderDetail;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lgy
 * @since 2021-09-17
 */
public interface IOrderService extends IService<Order> {

    Order seckill(User user, GoodsVo goodsVo);

    OrderDetail getOrderDetailById(Long orderId);

    Long getResult(User user, Long goodsId);

    boolean checkPath(String path, User user, Long goodsId);

    String createPath(User user, Long goodsId);

    boolean checkCaptcha(String captcha, User user, Long goodsId);
}

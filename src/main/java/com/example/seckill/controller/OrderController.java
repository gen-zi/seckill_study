package com.example.seckill.controller;


import com.example.seckill.pojo.User;
import com.example.seckill.service.IOrderService;
import com.example.seckill.vo.OrderDetail;
import com.example.seckill.vo.RespBean;
import com.example.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lgy
 * @since 2021-09-17
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @RequestMapping("detail")
    @ResponseBody
    public RespBean detail(User user, Long orderId){
        if (user == null){
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        OrderDetail detail = orderService.getOrderDetailById(orderId);
        return RespBean.success(detail);
    }

}

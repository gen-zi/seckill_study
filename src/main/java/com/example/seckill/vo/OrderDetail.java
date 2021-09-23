package com.example.seckill.vo;

import com.example.seckill.pojo.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lgy
 * @Description
 * @ClassName OrderDetail
 * @data 2021/9/22
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail {

    private GoodsVo goodsVo;

    private Order order;
}

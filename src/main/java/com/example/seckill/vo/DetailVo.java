package com.example.seckill.vo;

import com.example.seckill.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lgy
 * @Description
 * @ClassName DetailVo
 * @data 2021/9/21
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailVo {
    private GoodsVo goodsVo;

    private User user;

    private int seckillStatus;

    private int remainSeconds;
}

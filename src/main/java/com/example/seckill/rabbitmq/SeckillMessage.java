package com.example.seckill.rabbitmq;

import com.example.seckill.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lgy
 * @Description
 * @ClassName SeckillMessage
 * @data 2021/9/23
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeckillMessage {

    private User user;

    private Long goodsId;
}

package com.example.seckill.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lgy
 * @Description
 * @ClassName MQSender
 * @data 2021/9/23
 * @Version 1.0
 */
@Service
@Slf4j
public class MQSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(String msg){
        log.info("发送消息："+msg);
        //千万不要错用到convertAndSendAndReceive()，这个方法会等待消费者消费完返回，这样引入RabbitMQ将没有任何意义
        rabbitTemplate.convertAndSend("seckillExchanger", "seckill.msg", msg);
    }



}

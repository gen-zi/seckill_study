package com.example.seckill.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lgy
 * @Description
 * @ClassName RabbitMQConfig
 * @data 2021/9/22
 * @Version 1.0
 */
@Configuration
public class RabbitMQConfig {

    private static final String QUEUE = "seckillQueue";
    private static final String EXCHANGER = "seckillExchanger";


    @Bean
    public Queue queue(){
      return new Queue(QUEUE);
    }

    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(EXCHANGER);
    }

    @Bean
    public Binding binding(){
        return BindingBuilder.bind(queue()).to(topicExchange()).with("seckill.#");
    }

}

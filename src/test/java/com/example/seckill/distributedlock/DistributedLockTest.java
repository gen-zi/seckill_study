package com.example.seckill.distributedlock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

/**
 * @author lgy
 * @Description
 * @ClassName Test
 * @data 2021/9/23
 * @Version 1.0
 */
@SpringBootTest
public class DistributedLockTest {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @org.junit.jupiter.api.Test
    public void test() throws InterruptedException {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        Boolean lock = valueOperations.setIfAbsent("flag", "true");
        if (lock){
            System.out.println("以上锁");
            Thread.sleep(1000);
            redisTemplate.delete("flag");
        }else {
            System.out.println("为争取到锁");
        }
    }
}

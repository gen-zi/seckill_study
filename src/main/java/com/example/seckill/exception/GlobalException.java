package com.example.seckill.exception;

import com.example.seckill.vo.RespBeanEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author lgy
 * @Description 自定义全局异常
 * @ClassName GobalException
 * @data 2021/9/15
 * @Version 1.0
 */
@Data
@AllArgsConstructor
public class GlobalException extends RuntimeException {
    private RespBeanEnum respBeanEnum;
}

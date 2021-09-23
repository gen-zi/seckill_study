package com.example.seckill.vo;

import com.example.seckill.pojo.Goods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lgy
 * @Description
 * @ClassName GoodsVo
 * @data 2021/9/18
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsVo extends Goods {
    private BigDecimal seckillPrice;
    private int stockCount;
    private Date startDate;
    private Date endDate;

}

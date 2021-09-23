package com.example.seckill.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author lgy
 * @since 2021-09-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_goods")
public class Goods implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商品id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商品名字
     */
    private String goodsName;

    /**
     * 商品标题
     */
    private String goodsTitle;

    /**
     * 商品价格
     */
    private BigDecimal goodsPrice;

    /**
     * 商品库存，-1表无限制
     */
    private Integer goodsStock;

    /**
     * 商品图片
     */
    private String goodsImg;

    /**
     * 商品细节
     */
    private String goodsDetail;


}

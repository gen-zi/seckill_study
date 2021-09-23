package com.example.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.seckill.pojo.Goods;
import com.example.seckill.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lgy
 * @since 2021-09-17
 */
public interface GoodsMapper extends BaseMapper<Goods> {

    List<GoodsVo> getGoodsVoList();

    GoodsVo getGoodsVo(long goodsId);

}

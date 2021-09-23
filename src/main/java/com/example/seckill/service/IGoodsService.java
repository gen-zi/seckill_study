package com.example.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.seckill.pojo.Goods;
import com.example.seckill.vo.GoodsVo;
import org.springframework.ui.Model;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lgy
 * @since 2021-09-17
 */
public interface IGoodsService extends IService<Goods> {

    List<GoodsVo> getGoodsVoList();

    GoodsVo getGoodsVo(Long goodsId);

    void setSecKillStatusAndRemainSeconds(GoodsVo goods, Model model);
}

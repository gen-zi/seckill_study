package com.example.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.seckill.mapper.GoodsMapper;
import com.example.seckill.pojo.Goods;
import com.example.seckill.service.IGoodsService;
import com.example.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lgy
 * @since 2021-09-17
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public List<GoodsVo> getGoodsVoList() {
        return goodsMapper.getGoodsVoList();
    }

    public GoodsVo getGoodsVo(Long goodsId){
        return goodsMapper.getGoodsVo(goodsId);
    }

    /**
     * 设置秒杀商品状态， 0 未开始，1 进行中， 2 已结束
     * 设置秒杀倒计时，0 进行中， -1 已结束  剩余秒数 未开始
     * @param goods
     */
    @Override
    public void setSecKillStatusAndRemainSeconds(GoodsVo goods, Model model) {
        Date startDate = goods.getStartDate();
        Date endDate = goods.getEndDate();
        Date date = new Date();
        if (date.before(startDate)){
            int remainSeconds = (int) ((startDate.getTime() - date.getTime()) / 1000);
            model.addAttribute("secKillStatus", 0);
            model.addAttribute("remainSeconds", remainSeconds);
            return;
        }else if (date.after(endDate)){
            model.addAttribute("secKillStatus", 2);
            model.addAttribute("remainSeconds", -1);
            return;
        }
        model.addAttribute("secKillStatus", 1);
        model.addAttribute("remainSeconds", 0);
    }

}

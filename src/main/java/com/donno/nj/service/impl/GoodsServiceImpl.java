package com.donno.nj.service.impl;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.dao.GoodsDao;
import com.donno.nj.domain.Goods;
import com.donno.nj.service.GoodsService;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class GoodsServiceImpl implements GoodsService
{

    @Autowired
    private GoodsDao goodsDao;

    @Override
    public Optional<Goods> findByName(String name) {
        return Optional.fromNullable(goodsDao.findByName(name));
    }

    @Override
    @OperationLog(desc = "查询商品信息")
    public List<Goods> retrieve(Map params)
    {
        List<Goods> goods = goodsDao.getList(params);
        return goods;
    }

    @Override
    @OperationLog(desc = "查询商品数量")
    public Integer count(Map params) {
        return goodsDao.count(params);
    }

    @Override
    @OperationLog(desc = "创建商品信息")
    public void create(Goods goods)
    {
        goodsDao.insert(goods);
    }

    @Override
    @OperationLog(desc = "修改商品信息")
    public void update(Integer id, Goods newGoods)
    {
        newGoods.setId(id);

        /*更新数据*/
        goodsDao.update(newGoods);
    }

    @Override
    @OperationLog(desc = "删除商品信息")
    public void deleteById(Integer id)
    {
        /*删除关联子表：地址表*/
        goodsDao.deleteByIdx(id);
    }
}

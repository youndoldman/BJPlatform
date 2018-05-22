package com.donno.nj.service.impl;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.dao.*;
import com.donno.nj.domain.*;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.service.GoodsService;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class GoodsServiceImpl implements GoodsService
{

    @Autowired
    private GoodsDao goodsDao;

    @Autowired
    private GoodsTypeDao goodsTypeDao;

    @Autowired
    private OrderDetailDao orderDetailDao;

    @Autowired
    private AreaDao areaDao;


    @Override
    public Optional<Goods> findByCode(String code) {
        return Optional.fromNullable(goodsDao.findByCode(code));
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
        /*参数校验*/
        if (goods == null || goods.getCode() == null || goods.getName() == null ||
                goods.getCode().trim().length() == 0 || goods.getName().trim().length() == 0 ||
                goods.getSpecifications() == null || goods.getSpecifications().trim().length() == 0 ||
                goods.getUnit() == null || goods.getUnit().trim().length() == 0 ||
                goods.getPrice() == null  || goods.getStatus() == null
                )
        {
            throw new ServerSideBusinessException("商品信息不全，请补充商品信息！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*区域信息*/
        Area area = goods.getArea();
        if (area == null || area.getProvince() == null || area.getCity() == null || area.getCounty() == null)
        {
            throw new ServerSideBusinessException("缺少商品区域信息！", HttpStatus.NOT_ACCEPTABLE);
        }

        Area targetArea =  areaDao.findByArea(area.getProvince(),area.getCity(),area.getCounty());
        if(targetArea == null)
        {
           areaDao.insert(area);
        }
        else
        {
            area.setId(targetArea.getId());
        }

        /*商品类型信息校验*/
        if (goods.getGoodsType() != null || goods.getGoodsType().getCode() == null || goods.getGoodsType().getCode().trim().length() == 0)
        {
            GoodsType goodsType = goodsTypeDao.findByCode(goods.getGoodsType().getCode());
            if (goodsType != null)
            {
                goods.setGoodsType(goodsType);
            }
            else
            {
                throw new ServerSideBusinessException("商品类型信息错误！", HttpStatus.NOT_ACCEPTABLE);
            }
        }
        else
        {
            throw new ServerSideBusinessException("商品信息不能为空！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*商品是否已经存在*/
        if (findByCode(goods.getCode()).isPresent())
        {
            throw new ServerSideBusinessException("商品信息已经存在！", HttpStatus.CONFLICT);
        }

        goodsDao.insert(goods);
    }

    @Override
    @OperationLog(desc = "修改商品信息")
    public void update(String code, Goods newGoods)
    {
        /*参数校验*/
        if (code == null || code.trim().length() == 0 || newGoods == null)
        {
            throw new ServerSideBusinessException("商品信息不能为空！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*商品是否存在*/
        Goods goods = findByCode(code).get();
        if(goods == null)
        {
            throw new ServerSideBusinessException("商品信息不存在！", HttpStatus.NOT_FOUND);
        }
        else
        {
            newGoods.setId(goods.getId());
        }

        /*目标code*/
        if (newGoods.getCode() != null && newGoods.getCode().trim().length() > 0 )
        {
            if (code.equals(newGoods.getCode()))
            {
                newGoods.setCode(null);
            }
            else
            {
                /*目标代码是否存在*/
                if (findByCode(newGoods.getCode()).isPresent())
                {
                    throw new ServerSideBusinessException("商品信息已经存在！", HttpStatus.CONFLICT);
                }
            }
        }
        else
        {
            newGoods.setCode(null);
        }

        /*商品类型*/
        if (newGoods.getGoodsType() != null && newGoods.getGoodsType().getCode() != null && newGoods.getGoodsType().getCode().trim().length() > 0)
        {
            GoodsType goodsType = goodsTypeDao.findByCode(newGoods.getGoodsType().getCode());
            if (goodsType != null)
            {
                newGoods.setGoodsType(goodsType);
            }
            else
            {
                throw new ServerSideBusinessException("商品类型信息不存在！", HttpStatus.NOT_ACCEPTABLE);
            }
        }
        else
        {
            newGoods.setGoodsType(null);
        }

        /*更新数据*/
        goodsDao.update(newGoods);
    }

    @Override
    @OperationLog(desc = "删除商品信息")
    public void deleteById(Integer id)
    {
        Goods goods = goodsDao.findById(id);
        if (goods == null)
        {
            throw new ServerSideBusinessException("商品信息不存在！",HttpStatus.NOT_FOUND);
        }

        /*检查，如果有订单记录，则不允许删除*/
        Map params = new HashMap<String,String>();
        params.putAll(ImmutableMap.of("goods", goods));
        if (orderDetailDao.count(params) > 0)
        {
            throw new ServerSideBusinessException("商品已有销售记录，不能删除商品信息！",HttpStatus.FORBIDDEN);
        }

        goodsDao.deleteByIdx(id);
    }



    @Override
    @OperationLog(desc = "查询调价历史")
    public List<AdjustPriceHistory> retrieveAdjustPriceHistory(Map params)
    {
        List<AdjustPriceHistory> AdjustPriceHistorys = goodsDao.getAdjustPriceHistory(params);
        return AdjustPriceHistorys;
    }

}

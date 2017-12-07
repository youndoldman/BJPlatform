package com.donno.nj.service.impl;


import com.donno.nj.dao.GoodsTypeDao;
import com.donno.nj.dao.GoodsDao;
import com.donno.nj.domain.Goods;
import com.donno.nj.domain.GoodsType;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.service.GoodsTypeService;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GoodsTypeServiceImpl implements GoodsTypeService
{

    @Autowired
    private GoodsTypeDao goodsTypeDao;

    @Autowired
    private GoodsDao goodsDao;


    @Override
    public Optional<GoodsType> findByCode(String code) {
        return Optional.fromNullable(goodsTypeDao.findByCode(code));
    }

    @Override
    public List<GoodsType> retrieve(Map params)
    {
        return goodsTypeDao.getList(params);
    }

    @Override
    public Integer count(Map params) {
        return goodsTypeDao.count(params);
    }

    @Override
    public GoodsType create(GoodsType customerType)
    {
        /*参数校验*/
        if(customerType == null ||
           customerType.getCode() == null || customerType.getName() == null ||
           customerType.getCode().trim().length() == 0 || customerType.getName().trim().length() == 0  )
        {
            throw new ServerSideBusinessException("商品类型数据不能为空！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*检查商品类型是否存在*/
        if (findByCode(customerType.getCode()).isPresent())
        {
            throw new ServerSideBusinessException("商品类型已经存在！", HttpStatus.CONFLICT);
        }

        goodsTypeDao.insert(customerType);
        return customerType;
    }


    @Override
    public void update(String code, GoodsType newGoodsType)
    {
        /*参数校验*/
        if (code == null)
        {
            throw new ServerSideBusinessException("商品类型数据不能为空！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*参数校验*/
        if(newGoodsType == null || (newGoodsType.getCode() == null &&  newGoodsType.getName() == null) )
        {
            throw new ServerSideBusinessException("商品类型数据不能为空！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*当前商品类型是否存在*/
        GoodsType goodsType = findByCode(code).get();
        if (goodsType == null)
        {
            throw new ServerSideBusinessException("当前商品类型不存在！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*检查商品类型是否存在*/
        if (newGoodsType.getCode() != null && newGoodsType.getCode().trim().length() >0)
        {
            if (!code.equals(newGoodsType.getCode()))
            {

                if (findByCode(newGoodsType.getCode()).isPresent())
                {
                    throw new ServerSideBusinessException("商品类型已经存在！", HttpStatus.CONFLICT);
                }
            }
            else
            {
                newGoodsType.setCode(null);
            }
        }
        else
        {
            newGoodsType.setCode(null);
        }

        /**/
        if (newGoodsType.getName() == null || newGoodsType.getName().trim().length() == 0)
        {
            newGoodsType.setCode(null);
        }

        newGoodsType.setId(goodsType.getId());
        goodsTypeDao.update(newGoodsType);
    }

    @Override
    public  void deleteById(Integer id)
    {
        /*如有已经有该类型的商品存在，则不允许删除该商品类型*/
        Map params = new HashMap<String,String>();
        params.putAll(ImmutableMap.of("typeCode", id));

        if(goodsDao.count(params) > 0)
        {
            throw new ServerSideBusinessException("商品类型不能删除，请先删除该类型的商品信息！", HttpStatus.NOT_ACCEPTABLE);
        }

        goodsTypeDao.delete(id);
    }



}

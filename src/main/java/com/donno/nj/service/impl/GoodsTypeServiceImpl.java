package com.donno.nj.service.impl;


import com.donno.nj.dao.GoodsTypeDao;
import com.donno.nj.domain.GoodsType;
import com.donno.nj.service.GoodsTypeService;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class GoodsTypeServiceImpl implements GoodsTypeService
{

    @Autowired
    private GoodsTypeDao goodsTypeDao;

    @Override
    public Optional<GoodsType> findByCode(String code) {
        return Optional.fromNullable(goodsTypeDao.findByCode(code));
    }

    @Override
    public List<GoodsType> retrieve(Map params) {
        return goodsTypeDao.getList(params);
    }

    @Override
    public Integer count(Map params) {
        return goodsTypeDao.count(params);
    }

    @Override
    public GoodsType create(GoodsType customerType) {
        goodsTypeDao.insert(customerType);
        return customerType;
    }


    @Override
    public void update(Integer id, GoodsType newGoodsType)
    {
        /*更新基表数据*/
        newGoodsType.setId(id);
        goodsTypeDao.update(newGoodsType);
    }

    @Override
    public  void deleteById(Integer id)
    {
        goodsTypeDao.delete(id);
    }

}

package com.donno.nj.dao;


import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.Goods;

public interface GoodsDao extends BaseDao<Goods>
{
    Goods findByCode(String code);

    void deleteByIdx(Integer id);
}

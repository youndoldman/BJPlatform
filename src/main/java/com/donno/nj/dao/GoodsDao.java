package com.donno.nj.dao;


import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.Goods;

public interface GoodsDao extends BaseDao<Goods>
{
    Goods findByName(String name);

    void deleteByIdx(Integer id);
}

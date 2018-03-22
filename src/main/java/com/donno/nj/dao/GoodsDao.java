package com.donno.nj.dao;


import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.Goods;
import com.donno.nj.domain.AdjustPriceHistory;

import java.util.List;
import java.util.Map;

public interface GoodsDao extends BaseDao<Goods>
{
    Goods findByCode(String code);

    void deleteByIdx(Integer id);

    List<AdjustPriceHistory> getAdjustPriceHistory(Map m);
}

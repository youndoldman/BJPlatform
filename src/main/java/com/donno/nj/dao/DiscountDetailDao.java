package com.donno.nj.dao;


import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.AdjustPriceDetail;
import com.donno.nj.domain.DiscountDetail;

public interface DiscountDetailDao extends BaseDao<DiscountDetail>
{
    void deleteByDiscountStrategyIdx(Integer id);
}

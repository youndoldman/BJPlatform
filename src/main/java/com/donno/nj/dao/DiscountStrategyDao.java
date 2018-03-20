package com.donno.nj.dao;


import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.AdjustPriceSchedule;
import com.donno.nj.domain.DiscountStrategy;

public interface DiscountStrategyDao extends BaseDao<DiscountStrategy>
{
    DiscountStrategy findByName(String name);
}

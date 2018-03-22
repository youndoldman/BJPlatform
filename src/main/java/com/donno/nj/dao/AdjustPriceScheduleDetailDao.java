package com.donno.nj.dao;


import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.AdjustPriceDetail;
import com.donno.nj.domain.OrderDetail;

public interface AdjustPriceScheduleDetailDao extends BaseDao<AdjustPriceDetail>
{
    void deleteByScheduleIdx(Integer id);
}

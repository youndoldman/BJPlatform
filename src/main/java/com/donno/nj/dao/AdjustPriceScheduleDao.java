package com.donno.nj.dao;


import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.AdjustPriceSchedule;
import com.donno.nj.domain.Order;
import org.apache.ibatis.annotations.Param;

public interface AdjustPriceScheduleDao extends BaseDao<AdjustPriceSchedule>
{
    AdjustPriceSchedule findByName(String name);
}

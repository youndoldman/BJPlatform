package com.donno.nj.dao;


import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.OrderDetail;

public interface OrderDetailDao extends BaseDao<OrderDetail>
{
    void deleteByOrderIdx(Integer id);
}

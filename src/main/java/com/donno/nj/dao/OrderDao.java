package com.donno.nj.dao;


import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.Order;

public interface OrderDao extends BaseDao<Order>
{
    Order findBySn(String sn);
}

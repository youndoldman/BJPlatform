package com.donno.nj.dao;


import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.Order;
import org.apache.ibatis.annotations.Param;
public interface OrderDao extends BaseDao<Order>
{
    Order findBySn(String sn);

    Long insertDistatcher(@Param("orderIdx") Integer orderIdx,@Param("userIdx") Integer userIdx);
}

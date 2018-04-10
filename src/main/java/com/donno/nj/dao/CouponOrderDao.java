package com.donno.nj.dao;


import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.CouponOrder;
import com.donno.nj.domain.TicketOrder;

public interface CouponOrderDao extends BaseDao<CouponOrder>
{
    CouponOrder findByCouponIdx(Integer couponIdx);
}

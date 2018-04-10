package com.donno.nj.service;

import com.donno.nj.domain.CouponOrder;
import com.donno.nj.domain.TicketOrder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
public interface CouponOrderService
{
    List<CouponOrder> retrieve(Map params);

    Integer count(Map params);

    void create(CouponOrder couponOrder);

    void delete(Integer id);

}

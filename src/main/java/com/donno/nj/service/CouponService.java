package com.donno.nj.service;

import com.donno.nj.domain.Coupon;
import com.donno.nj.domain.Ticket;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
public interface CouponService
{
    List<Coupon> retrieve(Map params);

    Integer count(Map params);

    void create(Coupon coupon,Integer couponCount);

    void update(Integer id, Coupon newCoupon);

    void delete(Integer id);

}

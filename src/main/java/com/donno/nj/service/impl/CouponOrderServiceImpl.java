package com.donno.nj.service.impl;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.dao.*;
import com.donno.nj.domain.*;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.service.CouponOrderService;
import com.donno.nj.service.TicketOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CouponOrderServiceImpl implements CouponOrderService
{

    @Autowired
    private CouponOrderDao couponOrderDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    CouponDao couponDao;

    @Override
    @OperationLog(desc = "查询优惠券消费信息")
    public List<CouponOrder> retrieve(Map params)
    {
        List<CouponOrder> couponOrders = couponOrderDao.getList(params);
        return couponOrders;
    }

    @Override
    @OperationLog(desc = "查询优惠券消费信息")
    public Integer count(Map params) {
        return couponOrderDao.count(params);
    }

    public void checkCustomer(String userId)
    {
        if (userId == null || userId.trim().length() == 0)
        {
            throw new ServerSideBusinessException("客户信息不全，请补充！", HttpStatus.NOT_ACCEPTABLE);
        }

        User target = userDao.findByUserId(userId);
        if (target == null)
        {
            throw new ServerSideBusinessException("客户不存在！", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    public void checkCoupon(CouponOrder couponOrder)
    {
        if (couponOrder.getCouponIdx() == null )
        {
            throw new ServerSideBusinessException("优惠券信息不全，请补充！", HttpStatus.NOT_ACCEPTABLE);
        }

        Coupon target = couponDao.findById(couponOrder.getCouponIdx() );
        if (target == null)
        {
            throw new ServerSideBusinessException("优惠券信息不存在！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*该气票是否已被使用*/
        if (target.getCouponStatus() == TicketStatus.TSUsed)
        {
            throw new ServerSideBusinessException("该优惠券已经被使用！", HttpStatus.NOT_ACCEPTABLE);
        }
    }


    public void checkOrder(String OrderSn)
    {
        if (OrderSn == null )
        {
            throw new ServerSideBusinessException("订单信息不全，请补充！", HttpStatus.NOT_ACCEPTABLE);
        }

        Order target = orderDao.findBySn(OrderSn);
        if (target == null)
        {
            throw new ServerSideBusinessException("订单信息不存在！", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @Override
    @OperationLog(desc = "添加优惠券消费信息")
    public void create(CouponOrder couponOrder)
    {
        /*参数校验*/
        if (couponOrder == null)
        {
            throw new ServerSideBusinessException("优惠券消费信息不全，请补充信息！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*客户信息校验*/
        checkCustomer(couponOrder.getUserId());

        /*气票检查*/
        checkCoupon(couponOrder);

        /*订单检查*/
        checkOrder(couponOrder.getOrderSn());

        /*数据唯一性检查，同一张优惠券只能有一条消费记录*/
        checkDuplicatedData(couponOrder.getCouponIdx());

        couponOrderDao.insert(couponOrder);
    }


    public void checkDuplicatedData(Integer couponIdx)
    {
        if (couponOrderDao.findByCouponIdx(couponIdx)!= null)
        {
            throw new ServerSideBusinessException("该优惠券已经使用！", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @Override
    @OperationLog(desc = "删除优惠券消费信息")
    public void delete(Integer id)
    {
        CouponOrder couponOrder = couponOrderDao.findById(id);
        if (couponOrder == null)
        {
            throw new ServerSideBusinessException("优惠券消费记录信息不存在！", HttpStatus.NOT_ACCEPTABLE);
        }

        couponOrderDao.delete(id);
    }





}

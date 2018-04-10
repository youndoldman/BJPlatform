package com.donno.nj.service.impl;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.dao.*;
import com.donno.nj.domain.*;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.service.CouponService;
import com.donno.nj.service.TicketService;
import com.donno.nj.util.Clock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class CouponServiceImpl implements CouponService
{

    @Autowired
    private CouponDao couponDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private GoodsDao goodsDao;

    @Override
    @OperationLog(desc = "查询优惠券信息")
    public List<Coupon> retrieve(Map params)
    {
        List<Coupon> coupons = couponDao.getList(params);
        return coupons;
    }

    @Override
    @OperationLog(desc = "查询优惠券数量")
    public Integer count(Map params) {
        return couponDao.count(params);
    }



    public void checkCustomer(User customer)
    {
        if (customer == null || customer.getUserId() == null || customer.getUserId().trim().length() == 0)
        {
            throw new ServerSideBusinessException("客户信息不全，请补充！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*只有气票用户才能增加气票*/
        Customer target = customerDao.findByUserId(customer.getUserId());
        if (target == null)
        {
            throw new ServerSideBusinessException("客户不存在！", HttpStatus.NOT_ACCEPTABLE);
        }
        if (!target.getSettlementType().getCode().equals(ServerConstantValue.SETTLEMENT_TYPE_TICKET))
        {
            throw new ServerSideBusinessException("客户不是气票用户，不能使用气票！", HttpStatus.NOT_ACCEPTABLE);
        }

        customer.setId(target.getId());
    }

    public void checkOperator(User operator)
    {
        if (operator == null || operator.getUserId() == null || operator.getUserId().trim().length() == 0)
        {
            throw new ServerSideBusinessException("操作员信息不全，请补充！", HttpStatus.NOT_ACCEPTABLE);
        }

        User target = userDao.findByUserId(operator.getUserId());
        if (userDao.findByUserId(operator.getUserId()) == null)
        {
            throw new ServerSideBusinessException("操作员不存在！", HttpStatus.NOT_ACCEPTABLE);
        }
        operator.setId(target.getId());
    }


    public void checkSpec(String specCode)
    {
        if (specCode == null || specCode.trim().length() == 0)
        {
            throw new ServerSideBusinessException("缺少规格信息！", HttpStatus.NOT_ACCEPTABLE);
        }

        Goods target = goodsDao.findByCode(specCode);
        if (target == null)
        {
            throw new ServerSideBusinessException("规格信息不存在！", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    public void checkStartDate(Date startDate)
    {
        if (startDate == null)
        {
            throw new ServerSideBusinessException("缺少生效起始日期！", HttpStatus.NOT_ACCEPTABLE);
        }

        if (!Clock.beforeDate(new Date(),startDate))
        {
            throw new ServerSideBusinessException("起始日期早于当前日期！", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    public void checkEndDate(Date endDate)
    {
        if (endDate == null)
        {
            throw new ServerSideBusinessException("缺少生效结束日期！", HttpStatus.NOT_ACCEPTABLE);
        }

        if (!Clock.beforeDate(new Date(),endDate))
        {
            throw new ServerSideBusinessException("结束日期早于当前日期！", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @Override
    @OperationLog(desc = "添加优惠券信息")
    public void create(Coupon coupon)
    {
        /*参数校验*/
        if (coupon == null)
        {
            throw new ServerSideBusinessException("优惠券信息不全，请补充信息！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*客户信息校验*/
        checkCustomer(coupon.getCustomer());

        /*操作员信息校验*/
        checkOperator(coupon.getOperator());

        /*规格检查*/
        checkSpec(coupon.getSpecCode());

        /*生效日期检查*/
        checkStartDate(coupon.getStartDate());
        checkEndDate(coupon.getEndDate());

        couponDao.insert(coupon);
    }

    @Override
    @OperationLog(desc = "修改优惠券信息")
    public void update(Integer id, Coupon newCoupon)
    {
        /*参数校验*/
        if (newCoupon == null )
        {
            throw new ServerSideBusinessException("优惠券信息不能为空！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*气票是否存在*/
        Coupon coupon = couponDao.findById(id);
        if (coupon == null)
        {
            throw new ServerSideBusinessException("优惠券不存在！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*已经使用的气票不允许修改*/
        if(coupon.getCouponStatus() == TicketStatus.TSUsed)
        {
            throw new ServerSideBusinessException("优惠券已经使用，不允许修改！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*客户信息校验*/
        if (newCoupon.getCustomer() != null)
        {
            checkCustomer(newCoupon.getCustomer());
        }

        /*操作员信息校验*/
        if (newCoupon.getOperator() != null)
        {
            checkOperator(newCoupon.getOperator());
        }

        /*规格检查*/
        if (newCoupon.getSpecCode() != null)
        {
            checkSpec(newCoupon.getSpecCode());
        }

        /*生效日期检查*/
        if (newCoupon.getStartDate() != null)
        {
            checkStartDate(newCoupon.getStartDate());
        }
        if (newCoupon.getStartDate() != null)
        {
            checkEndDate(newCoupon.getEndDate());
        }

        /*更新数据*/
        newCoupon.setId(id);
        couponDao.update(newCoupon);
    }

    @Override
    @OperationLog(desc = "删除优惠券信息")
    public void delete(Integer id)
    {
        Coupon coupon = couponDao.findById(id);
        if (coupon == null)
        {
            throw new ServerSideBusinessException("优惠券信息不存在！", HttpStatus.NOT_FOUND);
        }

         /*已经使用的优惠券不允许删除*/
        if(coupon.getCouponStatus() == TicketStatus.TSUsed)
        {
            throw new ServerSideBusinessException("优惠券已经使用，不允许删除！", HttpStatus.NOT_ACCEPTABLE);
        }

        couponDao.delete(id);
    }





}

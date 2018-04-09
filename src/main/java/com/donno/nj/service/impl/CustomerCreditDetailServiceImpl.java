package com.donno.nj.service.impl;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.dao.CustomerCreditDetailDao;
import com.donno.nj.dao.OrderDao;
import com.donno.nj.dao.UserDao;
import com.donno.nj.domain.CustomerCreditDetail;
import com.donno.nj.domain.Order;
import com.donno.nj.domain.User;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.service.CustomerCreditDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CustomerCreditDetailServiceImpl implements CustomerCreditDetailService
{

    @Autowired
    private CustomerCreditDetailDao customerCreditDetailDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private OrderDao orderDao ;


    @Override
    @OperationLog(desc = "查询客户信用（欠款）记录信息")
    public List<CustomerCreditDetail> retrieve(Map params)
    {
        List<CustomerCreditDetail> customerCredits = customerCreditDetailDao.getList(params);
        return customerCredits;
    }

    @Override
    @OperationLog(desc = "查询客户信用（欠款）记录信息条数")
    public Integer count(Map params) {
        return customerCreditDetailDao.count(params);
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

    @Override
    @OperationLog(desc = "增加客户信用（欠款）记录")
    public void create(CustomerCreditDetail customerCreditDetail)
    {
        /*参数校验*/
        if (customerCreditDetail == null)
        {
            throw new ServerSideBusinessException("赊账记录信息不全，请补充信息！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*客户信息校验*/
        checkCustomer(customerCreditDetail.getUserId());

        /*订单信息校验*/
        checkOrder(customerCreditDetail.getOrderSn());

        customerCreditDetailDao.insert(customerCreditDetail);
    }



    @Override
    @OperationLog(desc = "删除欠款记录信息")
    public void deleteById(Integer id)
    {
        CustomerCreditDetail customerCreditDetail = customerCreditDetailDao.findById(id);
        if (customerCreditDetail == null)
        {
            throw new ServerSideBusinessException("欠款记录信息不存在！", HttpStatus.NOT_ACCEPTABLE);
        }

        customerCreditDetailDao.delete(id);
    }
}

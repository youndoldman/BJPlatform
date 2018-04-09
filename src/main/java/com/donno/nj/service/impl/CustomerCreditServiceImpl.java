package com.donno.nj.service.impl;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.dao.CustomerCreditDao;
import com.donno.nj.dao.GoodsDao;
import com.donno.nj.dao.TicketDao;
import com.donno.nj.dao.UserDao;
import com.donno.nj.domain.*;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.service.CustomerCreditService;
import com.donno.nj.service.TicketService;
import com.donno.nj.util.Clock;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class CustomerCreditServiceImpl implements CustomerCreditService
{

    @Autowired
    private CustomerCreditDao customerCreditDao;

    @Autowired
    private UserDao userDao;

    @Override
    @OperationLog(desc = "查询客户信用（欠款）信息")
    public List<CustomerCredit> retrieve(Map params)
    {
        List<CustomerCredit> customerCredits = customerCreditDao.getList(params);
        return customerCredits;
    }

    @Override
    @OperationLog(desc = "查询客户信用记录条数")
    public Integer count(Map params) {
        return customerCreditDao.count(params);
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


    public void create(CustomerCredit customerCredit)
    {
        /*参数校验*/
        if (customerCredit == null)
        {
            throw new ServerSideBusinessException("赊账信息不全，请补充信息！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*客户信息校验*/
        checkCustomer(customerCredit.getUserId());

        customerCreditDao.insert(customerCredit);
    }

    @Override
    @OperationLog(desc = "修改客户欠款信息")
    public void update(Integer id, CustomerCredit newCustomerCredit)
    {
        /*参数校验*/
        if ( newCustomerCredit == null)
        {
            throw new ServerSideBusinessException("客户欠款信息不能为空！", HttpStatus.NOT_ACCEPTABLE);
        }

        if (id == null)
        {
            create(newCustomerCredit) ;
        }
        else
        {
            CustomerCredit customerCredit = customerCreditDao.findById(id);
            if (customerCredit == null)
            {
                create(newCustomerCredit) ;
            }
            else
            {
                newCustomerCredit.setId(id);
                customerCreditDao.update(newCustomerCredit);
            }
        }
    }

    @Override
    @OperationLog(desc = "删除欠款信息")
    public void deleteById(Integer id)
    {
        CustomerCredit customerCredit = customerCreditDao.findById(id);
        if (customerCredit == null)
        {
            throw new ServerSideBusinessException("欠款信息不存在！", HttpStatus.NOT_ACCEPTABLE);
        }

        customerCreditDao.delete(id);
    }


}

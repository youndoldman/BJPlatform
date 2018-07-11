package com.donno.nj.service.impl;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.dao.*;
import com.donno.nj.domain.*;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.service.CustomerCallInService;
import com.donno.nj.service.CustomerService;
import com.google.common.collect.ImmutableMap;
import org.h2.command.ddl.CreateUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class CustomerCallInServiceImpl implements CustomerCallInService
{

    @Autowired
    private CustomerCallInDao customerCallInDao;

    @Autowired
    private CustomerDao customerDao;

    @Override
    @OperationLog(desc = "查询客户呼入信息")
    public List<CustomerCallIn> retrieve(Map params)
    {
        List<CustomerCallIn> customerCallIns = customerCallInDao.getList(params);
        return customerCallIns;
    }

    @Override
    @OperationLog(desc = "查询客户数量")
    public Integer count(Map params) {
        return customerCallInDao.count(params);
    }

    @Override
    @OperationLog(desc = "创建客户呼入信息")
    public void create(String userId,CustomerCallIn customerCallIn)
    {
        /*userId参数校验*/
        if (userId == null || userId.trim().length() == 0)
        {
            throw new ServerSideBusinessException("请填写用户信息",HttpStatus.NOT_ACCEPTABLE);
        }

        /*phone参数校验*/
        if (customerCallIn == null || customerCallIn.getPhone() == null || customerCallIn.getPhone().trim().length() == 0  )
        {
            throw new ServerSideBusinessException("请填写呼入号码信息",HttpStatus.NOT_ACCEPTABLE);
        }

         /*地址参数校验 */
        if (customerCallIn.getProvince() == null || customerCallIn.getProvince().trim().length() == 0 ||
             customerCallIn.getCity() == null || customerCallIn.getCity().trim().length() == 0  ||
                customerCallIn.getCounty() == null || customerCallIn.getCounty().trim().length() == 0  ||
                customerCallIn.getDetail() == null || customerCallIn.getDetail().trim().length() == 0 )
        {
            throw new ServerSideBusinessException("请填写地址信息",HttpStatus.NOT_ACCEPTABLE);
        }

        /*检查用户会否存在*/
        Customer user = customerDao.findByCstUserId(userId);
        if (user == null)
        {
            throw new ServerSideBusinessException("用户信息错误",HttpStatus.NOT_ACCEPTABLE);
        }

        customerCallIn.setCustomer(user);
        customerCallInDao.insert(customerCallIn);

        /*检查呼叫信息是否已经存在，若存在，更新时间*/
        Map params = new HashMap<String,String>();
        params.putAll(ImmutableMap.of("phone", customerCallIn.getPhone()));
        params.putAll(ImmutableMap.of("userId", userId));
        params.putAll(ImmutableMap.of("province", customerCallIn.getProvince()));
        params.putAll(ImmutableMap.of("city", customerCallIn.getCity()));
        params.putAll(ImmutableMap.of("county", customerCallIn.getCounty()));
        params.putAll(ImmutableMap.of("detail", customerCallIn.getDetail()));
        List<CustomerCallIn> customerCallIns = customerCallInDao.getList(params);
        if (customerCallIns.size() == 0)
        {
            customerCallIn.setCustomer(user);
            customerCallInDao.insert(customerCallIn);
        }
//        else
//        {
//            // customerCallInDao.update(customerCallIn);
//        }
    }


    @Override
    @OperationLog(desc = "删除客户呼入信息")
    public void delete(Map params)
    {
        customerCallInDao.delete(params);
    }

}


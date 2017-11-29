package com.donno.nj.service.impl;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.dao.*;
import com.donno.nj.domain.*;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.service.CustomerCallInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class CustomerCallInServiceImpl implements CustomerCallInService
{

    @Autowired
    private CustomerCallInDao customerCallInDao;

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
    public void create(CustomerCallIn customerCallIn)
    {
        customerCallInDao.insert(customerCallIn);
    }


    @Override
    @OperationLog(desc = "删除客户呼入信息")
    public void delete(Map params)
    {
        customerCallInDao.delete(params);
    }

}


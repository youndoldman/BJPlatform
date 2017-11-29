package com.donno.nj.service.impl;


import com.donno.nj.dao.CustomerLevelDao;
import com.donno.nj.domain.CustomerLevel;
import com.donno.nj.domain.CustomerSource;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.service.CustomerLevelService;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CustomerLevelServiceImpl implements CustomerLevelService
{

    @Autowired
    private CustomerLevelDao customerLevelDao;

    @Override
    public Optional<CustomerLevel> findByCode(String code) {
        return Optional.fromNullable(customerLevelDao.findByCode(code));
    }

    @Override
    public List<CustomerLevel> retrieve(Map params) {
        return customerLevelDao.getList(params);
    }

    @Override
    public Integer count(Map params) {
        return customerLevelDao.count(params);
    }

    @Override
    public CustomerLevel create(CustomerLevel customerLevel)
    {
        /*参数校验*/
        if (customerLevel.getCode() == null || customerLevel.getCode().trim().length() ==0)
        {
            throw new ServerSideBusinessException("请填写客户级别编码信息", HttpStatus.NOT_ACCEPTABLE);
        }

        if (customerLevel.getName() == null || customerLevel.getName().trim().length() == 0)
        {
            throw new ServerSideBusinessException("请填写客户级别名称信息", HttpStatus.NOT_ACCEPTABLE);
        }

        /*检查是否已经存在*/
        if (customerLevelDao.findByCode(customerLevel.getCode()) != null)
        {
            throw new ServerSideBusinessException("客户级别编码信息已经存在", HttpStatus.CONFLICT);
        }

        customerLevelDao.insert(customerLevel);
        return customerLevel;
    }


    @Override
    public void update(Integer id, CustomerLevel newCustomerLevel)
    {
        newCustomerLevel.setId(id);

        /*目的客户类型编码是否已经存在*/
        if ( newCustomerLevel.getCode() != null && newCustomerLevel.getCode().trim().length() !=0)
        {
            CustomerLevel customerLevel = customerLevelDao.findByCode(newCustomerLevel.getCode());
            if (customerLevel != null)
            {
                if (customerLevel.getId() != id)
                {
                    throw new ServerSideBusinessException("客户来源编码信息已经存在", HttpStatus.CONFLICT);
                }
                else
                {
                    newCustomerLevel.setCode(null);
                }
            }
        }

        customerLevelDao.update(newCustomerLevel);
    }

    @Override
    public void delete(Map params)
    {
        customerLevelDao.delete(params);
    }

}

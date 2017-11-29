package com.donno.nj.service.impl;


import com.donno.nj.dao.CustomerSourceDao;
import com.donno.nj.domain.CustomerSource;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.service.CustomerSourceService;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.google.common.base.Optional;
import java.util.List;
import java.util.Map;

@Service
public class CustomerSourceServiceImpl implements CustomerSourceService
{

    @Autowired
    private CustomerSourceDao customerSourceDao;

    @Override
    public Optional<CustomerSource> findByCode(String code) {
        return Optional.fromNullable(customerSourceDao.findByCode(code));
    }

    @Override
    public List<CustomerSource> retrieve(Map params) {
        return customerSourceDao.getList(params);
    }

    @Override
    public Integer count(Map params) {
        return customerSourceDao.count(params);
    }

    @Override
    public CustomerSource create(CustomerSource customerSource)
    {
        /*参数校验*/
        if (customerSource.getCode() == null || customerSource.getCode().trim().length() ==0)
        {
            throw new ServerSideBusinessException("请填写客户来源编码信息", HttpStatus.NOT_ACCEPTABLE);
        }

        if (customerSource.getName() == null || customerSource.getName().trim().length() == 0)
        {
            throw new ServerSideBusinessException("请填写客户来源名称信息", HttpStatus.NOT_ACCEPTABLE);
        }

        /*检查是否已经存在*/
        if (customerSourceDao.findByCode(customerSource.getCode()) != null)
        {
            throw new ServerSideBusinessException("客户来源编码信息已经存在", HttpStatus.CONFLICT);
        }

        customerSourceDao.insert(customerSource);
        return customerSource;
    }


    @Override
    public void update(Integer id, CustomerSource newCustomerSource)
    {
        newCustomerSource.setId(id);

        /*目的客户类型编码是否已经存在*/
        if ( newCustomerSource.getCode() != null && newCustomerSource.getCode().trim().length() !=0)
        {
            CustomerSource customerSource = customerSourceDao.findByCode(newCustomerSource.getCode());
            if (customerSource != null)
            {
                if (customerSource.getId() != id)
                {
                    throw new ServerSideBusinessException("客户来源编码信息已经存在", HttpStatus.CONFLICT);
                }
                else
                {
                    newCustomerSource.setCode(null);
                }
            }
        }

        customerSourceDao.update(newCustomerSource);
    }

    @Override
    public void delete(Map params)
    {
        customerSourceDao.delete(params);
    }

}

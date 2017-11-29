package com.donno.nj.service.impl;


import com.donno.nj.dao.CustomerTypeDao;
import com.donno.nj.domain.CustomerType;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.service.CustomerTypeService;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class CustomerTypeServiceImpl implements CustomerTypeService
{

    @Autowired
    private CustomerTypeDao customerTypeDao;

    @Override
    public Optional<CustomerType> findByCode(String code) {
        return Optional.fromNullable(customerTypeDao.findByCode(code));
    }

    @Override
    public List<CustomerType> retrieve(Map params) {
        return customerTypeDao.getList(params);
    }

    @Override
    public Integer count(Map params) {
        return customerTypeDao.count(params);
    }

    @Override
    public CustomerType create(CustomerType customerType)
    {
        /*参数校验*/
        if (customerType.getCode() == null || customerType.getCode().trim().length() ==0)
        {
            throw new ServerSideBusinessException("请填写客户类型编码信息", HttpStatus.NOT_ACCEPTABLE);
        }

        if (customerType.getName() == null || customerType.getName().trim().length() == 0)
        {
            throw new ServerSideBusinessException("请填写客户类型名称信息", HttpStatus.NOT_ACCEPTABLE);
        }

        /*检查是否已经存在*/
        if (customerTypeDao.findByCode(customerType.getCode()) != null)
        {
            throw new ServerSideBusinessException("客户类型编码信息已经存在", HttpStatus.CONFLICT);
        }

        customerTypeDao.insert(customerType);
        return customerType;
    }


    @Override
    public void update(Integer id, CustomerType newCustomerType)
    {
        newCustomerType.setId(id);

        /*目的客户类型编码是否已经存在*/
        if ( newCustomerType.getCode() != null && newCustomerType.getCode().trim().length() !=0)
        {
            CustomerType customerType = customerTypeDao.findByCode(newCustomerType.getCode());
            if (customerType != null)
            {
                if (customerType.getId() != id)
                {
                    throw new ServerSideBusinessException("客户类型编码信息已经存在", HttpStatus.CONFLICT);
                }
                else
                {
                    newCustomerType.setCode(null);
                }
            }
        }

        customerTypeDao.update(newCustomerType);
    }

    @Override
    public void delete(Map params)
    {
        customerTypeDao.delete(params);
    }

}

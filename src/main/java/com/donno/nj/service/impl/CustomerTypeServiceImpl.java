package com.donno.nj.service.impl;


import com.donno.nj.dao.CustomerTypeDao;
import com.donno.nj.domain.CustomerType;
import com.donno.nj.service.CustomerTypeService;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
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
    public CustomerType create(CustomerType customerType) {
        customerTypeDao.insert(customerType);
        return customerType;
    }


    @Override
    public void update(CustomerType customerType, CustomerType newCustomerType)
    {

    }

    @Override
    public void delete(CustomerType customerType)
    {
        customerTypeDao.delete(customerType.getId());
    }

}

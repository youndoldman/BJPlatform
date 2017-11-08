package com.donno.nj.service.impl;

import com.donno.nj.dao.CustomerSourceDao;
import com.donno.nj.domain.CustomerSource;
import com.donno.nj.service.CustomerSourceService;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public CustomerSource create(CustomerSource customerSource) {
        customerSourceDao.insert(customerSource);
        return customerSource;
    }


    @Override
    public void update(CustomerSource customerSource, CustomerSource newCustomerSource)
    {

    }

    @Override
    public void delete(CustomerSource customerSource)
    {
        customerSourceDao.delete(customerSource.getId());
    }
}

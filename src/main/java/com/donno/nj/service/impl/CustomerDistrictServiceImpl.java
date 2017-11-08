package com.donno.nj.service.impl;

import com.donno.nj.dao.CustomerDistrictDao;
import com.donno.nj.domain.CustomerDistrict;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.donno.nj.service.CustomerDistrictService;

import java.util.List;
import java.util.Map;



@Service
public class CustomerDistrictServiceImpl implements CustomerDistrictService
{
    @Autowired
    private CustomerDistrictDao customerDistrictDao;

    @Override
    public Optional<CustomerDistrict> findByCode(String code) {
        return Optional.fromNullable(customerDistrictDao.findByCode(code));
    }

    @Override
    public List<CustomerDistrict> retrieve(Map params) {
        return customerDistrictDao.getList(params);
    }

    @Override
    public Integer count(Map params) {
        return customerDistrictDao.count(params);
    }

    @Override
    public CustomerDistrict create(CustomerDistrict customerDistrict) {
        customerDistrictDao.insert(customerDistrict);
        return customerDistrict;
    }

    @Override
    public void update(CustomerDistrict customerDistrict, CustomerDistrict newCustomerDistrict)
    {

    }

    @Override
    public void delete(CustomerDistrict customerDistrict)
    {
        customerDistrictDao.delete(customerDistrict.getId());
    }
}

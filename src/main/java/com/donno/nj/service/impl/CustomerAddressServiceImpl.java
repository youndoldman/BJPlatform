package com.donno.nj.service.impl;


import com.donno.nj.dao.CustomerAddressDao;
import com.donno.nj.domain.CustomerAddress;
import com.donno.nj.service.CustomerAddressService;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CustomerAddressServiceImpl implements CustomerAddressService
{
    @Autowired
    private CustomerAddressDao customerAddressDao;


    @Override
    public void create(CustomerAddress address)
    {
        customerAddressDao.insert(address);
    }


    @Override
    public void update(CustomerAddress address, CustomerAddress newAddress)
    {
        newAddress.setCustomerIdx(address.getCustomerIdx());
        customerAddressDao.update(newAddress);
    }


    @Override
    public void delete(Integer id)
    {
        customerAddressDao.deleteById(id);
    }

    @Override
    public void deleteByUserId(Integer customerIdx)
    {
        customerAddressDao.deleteByUserId(customerIdx);
    }


}

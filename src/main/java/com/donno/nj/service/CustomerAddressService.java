package com.donno.nj.service;

import com.donno.nj.domain.CustomerAddress;


import java.util.List;
import java.util.Map;


public interface CustomerAddressService
{
    void create(CustomerAddress address);

    void update(CustomerAddress address, CustomerAddress newAddress);

    void delete(Integer id);
    void deleteByUserId(Integer customerIdx);
}

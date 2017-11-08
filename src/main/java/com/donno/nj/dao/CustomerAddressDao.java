package com.donno.nj.dao;


import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.CustomerAddress;

public interface CustomerAddressDao extends BaseDao<CustomerAddress>
{
    void deleteByUserId(Integer userId);
    void deleteById(Integer id);
}

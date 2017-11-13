package com.donno.nj.dao;


import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.CustomerAddress;

public interface CustomerAddressDao extends BaseDao<CustomerAddress>
{
    void deleteByUserIdx(Integer userIdx);
    void deleteById(Integer id);

    CustomerAddress findByCustomerIdx(Integer userIdx);

}

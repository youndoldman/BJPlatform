package com.donno.nj.dao;


import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.Customer;

public interface CustomerDao extends BaseDao<Customer>
{
    Customer findByNumber(String number);
    Customer findByUserId(String userIdentity);

    void deleteByUserId(String userId);

}

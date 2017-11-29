package com.donno.nj.dao;


import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.Customer;

public interface CustomerDao extends BaseDao<Customer>
{
    Customer findByUserId(String userId);

    void deleteByUserIdx(Integer id);


}

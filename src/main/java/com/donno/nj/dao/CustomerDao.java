package com.donno.nj.dao;


import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.Customer;

import java.util.List;
import java.util.Map;

public interface CustomerDao extends BaseDao<Customer>
{
//    Customer findByUserId(String userId);

    void deleteByUserIdx(Integer id);

    Customer findByUserPhone(String userPhone);

    Customer findByCstUserId(String userId);


    void updateLeakWarningTime(Map params);



    List<String> getPhones(Map m);
}

package com.donno.nj.dao;

import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.CustomerCompany;
import com.donno.nj.domain.CustomerSource;

/**
 * Created by T470P on 2017/10/30.
 */
public interface CustomerCompanyDao extends BaseDao<CustomerCompany>
{
    CustomerCompany findByCode(String code);
    CustomerCompany findByName(String name);
    String getNextCode();

}

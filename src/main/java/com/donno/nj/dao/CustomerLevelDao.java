package com.donno.nj.dao;

import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.CustomerLevel;

/**
 * Created by T470P on 2017/10/30.
 */
public interface CustomerLevelDao extends BaseDao<CustomerLevel>
{
    CustomerLevel findByCode(String code);
}

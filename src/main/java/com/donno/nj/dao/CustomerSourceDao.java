package com.donno.nj.dao;

import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.CustomerSource;

/**
 * Created by T470P on 2017/10/30.
 */
public interface CustomerSourceDao extends BaseDao<CustomerSource>
{
    CustomerSource findByCode(String code);
}

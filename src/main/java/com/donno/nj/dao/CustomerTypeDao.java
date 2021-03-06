package com.donno.nj.dao;

import com.donno.nj.domain.CustomerType;
import com.donno.nj.dao.base.BaseDao;
import com.google.common.base.Optional;

import java.util.Map;

public interface CustomerTypeDao extends BaseDao<CustomerType>
{
    CustomerType findByCode(String code);
}

package com.donno.nj.dao;

import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.SecurityType;

public interface SecurityTypeDao extends BaseDao<SecurityType>
{
    SecurityType findByCode(String code);
}

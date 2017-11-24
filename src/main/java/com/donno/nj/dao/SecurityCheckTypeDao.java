package com.donno.nj.dao;

import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.SecurityCheckType;

public interface SecurityCheckTypeDao extends BaseDao<SecurityCheckType>
{
    SecurityCheckType findByCode(String code);
}

package com.donno.nj.dao;

import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.MendType;

public interface MendTypeDao extends BaseDao<MendType>
{
    MendType findByCode(String code);
}

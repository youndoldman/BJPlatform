package com.donno.nj.dao;

import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.GasCylinderSpec;
import com.donno.nj.domain.GasCynFactory;

public interface GasCynFactoryDao extends BaseDao<GasCynFactory>
{
    GasCynFactory findByCode(String code);
}

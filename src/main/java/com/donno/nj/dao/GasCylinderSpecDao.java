package com.donno.nj.dao;

import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.GasCylinderSpec;

public interface GasCylinderSpecDao extends BaseDao<GasCylinderSpec>
{
    GasCylinderSpec findByCode(String code);
}

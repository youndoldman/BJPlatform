package com.donno.nj.dao;

import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.CustomerType;
import com.donno.nj.domain.SettlementType;

public interface SettlementTypeDao extends BaseDao<SettlementType>
{
    SettlementType findByCode(String code);
}

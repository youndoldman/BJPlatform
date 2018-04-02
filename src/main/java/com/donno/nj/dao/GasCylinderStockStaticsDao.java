package com.donno.nj.dao;

import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.GasCynServiceStatus;
import com.donno.nj.domain.GasCylinderStockStatics;


import java.util.List;


public interface GasCylinderStockStaticsDao extends BaseDao<GasCylinderStockStatics>
{
    List<GasCylinderStockStatics> getStatics(Integer gasCynServiceStatus);
}

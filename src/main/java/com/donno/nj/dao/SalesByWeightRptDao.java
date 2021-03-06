package com.donno.nj.dao;

import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.SalesRptByWeight;

import java.util.List;
import java.util.Map;

public interface SalesByWeightRptDao extends BaseDao<SalesRptByWeight>
{
    List<SalesRptByWeight>  getSaleRptByWeight(Map param);
}

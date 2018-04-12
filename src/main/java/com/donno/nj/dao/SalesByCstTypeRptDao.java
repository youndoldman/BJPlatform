package com.donno.nj.dao;

import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.SalesRpt;

import java.util.List;
import java.util.Map;

public interface SalesByCstTypeRptDao extends BaseDao<SalesRpt>
{
    List<SalesRpt>  getSaleRptByCstType(Map param);

    Integer countSaleRptByCstType(Map param);
}

package com.donno.nj.dao;

import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.SalesRpt;
import java.util.Map;
import java.util.List;

public interface SalesByPayTypeRptDao extends BaseDao<SalesRpt>
{
    List<SalesRpt>  getSaleRptByPayType(Map param);

    Integer countSaleRptByPayType(Map param);

}

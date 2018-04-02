package com.donno.nj.dao;

import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.SalesRpt;
import java.util.Map;
import java.util.List;

public interface SalesRptDao extends BaseDao<SalesRpt>
{
    List<SalesRpt>  getDailyRpt(Map param);

    Integer countDailyRpt(Map param);
}

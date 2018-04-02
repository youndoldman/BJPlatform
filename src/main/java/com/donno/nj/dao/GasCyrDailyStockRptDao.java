package com.donno.nj.dao;


import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.GasCylinderStockInOutStatics;
import com.donno.nj.domain.GasCyrDailyStockRpt;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface GasCyrDailyStockRptDao extends BaseDao<GasCyrDailyStockRpt>
{
    GasCyrDailyStockRpt  findByDaily(@Param("departmentCode") String departmentCode,@Param("specCode") String specCode,@Param("date") Date date);
}

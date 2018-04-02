package com.donno.nj.dao;


import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.GasCyrSaleContactsRpt;
import org.apache.ibatis.annotations.Param;


import java.util.Date;
import java.util.List;
import java.util.Map;

public interface GasCyrSaleContactsRptDao extends BaseDao<GasCyrSaleContactsRpt>
{
    List<GasCyrSaleContactsRpt> getDailyRpt(Map params);
    Integer countDailyRpt(Map param);

    GasCyrSaleContactsRpt findByDaily(@Param("departmentCode") String departmentCode, @Param("goodsCode") String goodsCode, @Param("date") Date date);
}

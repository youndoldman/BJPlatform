package com.donno.nj.dao;
import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.GasCylinderStockInOutStatics;
import java.util.List;
import java.util.Map;

public interface GasCylinderStockInOutStaticsDao extends BaseDao<GasCylinderStockInOutStatics>
{
    List<GasCylinderStockInOutStatics> getStatics(Map m);
}

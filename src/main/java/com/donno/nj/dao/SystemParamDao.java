package com.donno.nj.dao;

import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.SystemParam;


public interface SystemParamDao extends BaseDao<SystemParam>
{
    Integer getDispatchRange();
    Integer getMaxDispatchRange();
    Integer getTrayWarningWeight();
    Float getGasTareDfferWeight();

    Integer getOrderOverTime();


}

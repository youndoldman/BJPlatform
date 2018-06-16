package com.donno.nj.dao;


import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.GasCynTray;

public interface GasCynTrayDao extends BaseDao<GasCynTray>
{
    GasCynTray findByNumber(String number);
}

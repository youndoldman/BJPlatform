package com.donno.nj.dao;


import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.GasCylinder;


public interface GasCylinderDao extends BaseDao<GasCylinder>
{
    GasCylinder findByNumber(String number);

    void deleteByIdx(Integer id);

    void updateSvcStatus(Integer id,Integer serviceStatus);

}

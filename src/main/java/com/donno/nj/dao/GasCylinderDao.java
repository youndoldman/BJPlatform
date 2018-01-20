package com.donno.nj.dao;


import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.GasCylinder;
import org.apache.ibatis.annotations.Param;


public interface GasCylinderDao extends BaseDao<GasCylinder>
{
    GasCylinder findByNumber(String number);

    void deleteByIdx(Integer id);

    void updateSvcStatus(@Param("id") Integer id, @Param("serviceStatus") Integer serviceStatus);

}

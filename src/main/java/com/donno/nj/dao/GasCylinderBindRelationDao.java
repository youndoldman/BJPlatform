package com.donno.nj.dao;


import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.GasCylinder;
import com.donno.nj.domain.GasCylinderBindRelation;
import com.donno.nj.domain.LocationDevice;
import org.apache.ibatis.annotations.Param;

public interface GasCylinderBindRelationDao extends BaseDao<GasCylinderBindRelation>
{
    void deleteByIdx(Integer id);

    void bindLocationDev(@Param("gasCylinderIdx") Integer gasCylinderIdx, @Param("locationDevIdx") Integer locationDevIdx);
    void unBindLocationDev(@Param("gasCylinderIdx") Integer gasCylinderIdx, @Param("locationDevIdx") Integer locationDevIdx);

    LocationDevice   findLocateDevByCylinderId(Integer gasCylinderIdx);
    GasCylinder  findGasCylinderByLocateDevId(Integer locationDevIdx)  ;

    GasCylinderBindRelation findBindRelation(@Param("gasCylinderIdx") Integer gasCylinderId,@Param("locationDevIdx") Integer locationDevIdx)  ;
}

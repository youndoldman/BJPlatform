package com.donno.nj.dao;


import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.GasCylinder;
import com.donno.nj.domain.Location;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


public interface GasCylinderDao extends BaseDao<GasCylinder>
{
    GasCylinder findByNumber(String number);

    void deleteByIdx(Integer id);

    void updateSvcStatus(@Param("id") Integer id, @Param("serviceStatus") Integer serviceStatus);

    List<GasCylinder> getListByCenterRange(@Param("longitude") Double longitude, @Param("latitude") Double latitude);
    List<Location> getLocations(Map m);

}

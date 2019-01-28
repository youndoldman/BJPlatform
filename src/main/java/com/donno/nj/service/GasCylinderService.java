package com.donno.nj.service;

import com.donno.nj.domain.GasCylinder;
import com.donno.nj.domain.Location;
import com.donno.nj.domain.LocationDevice;
import com.google.common.base.Optional;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
public interface GasCylinderService
{
    Optional<GasCylinder> findByNumber(String Number);

    List<GasCylinder> retrieve(Map params);

    Integer count(Map params);

    void create(GasCylinder gasCylinder);

    void update(String number, GasCylinder newGasCylinder);

    void updateSvcStatus(String number,Integer serviceStatus,String srcUserId,String targetUserId,Boolean enableForce,String note);

    void deleteById(Integer id);

    void bindLocationDev(String gasCylinderNumber,String locationDeviceNumber);

    void unBindLocationDev(String gasCylinderNumber,String locationDeviceNumber);

    //查询50公里内的钢瓶
    List<GasCylinder> getListByCenterRange(Double longitude,Double latitude);

    //查询钢瓶的经纬度
    List<Location> getLocations(Map params);



}
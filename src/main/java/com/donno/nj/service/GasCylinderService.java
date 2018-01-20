package com.donno.nj.service;

import com.donno.nj.domain.GasCylinder;
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

    void updateSvcStatus(String number,Integer serviceStatus,String srcUserId,String targetUserId);

    void deleteById(Integer id);

    void bindLocationDev(String gasCylinderNumber,String locationDeviceNumber);

    void unBindLocationDev(String gasCylinderNumber,String locationDeviceNumber);
}
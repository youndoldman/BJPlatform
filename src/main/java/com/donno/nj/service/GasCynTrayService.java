package com.donno.nj.service;

import com.donno.nj.domain.GasCynTray;
import com.donno.nj.domain.Ticket;
import com.google.common.base.Optional;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
public interface GasCynTrayService
{
    List<GasCynTray> retrieve(Map params);

    Integer count(Map params);

    void create(GasCynTray gasCynTray);

    void update(String number, GasCynTray newGasCynTray);

    void deleteById(Integer id);

    Optional<GasCynTray> findById(Integer id);


    void bind(String trayNumber,String userId);

    void unBind(String trayNumber,String userId);

    //消除托盘的报警状态
    void removeWaningStatus(String number);


}

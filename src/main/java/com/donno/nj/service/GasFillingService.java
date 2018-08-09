package com.donno.nj.service;

import com.donno.nj.domain.GasFilling;
import com.donno.nj.domain.Ticket;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
public interface GasFillingService
{
    List<GasFilling> retrieve(Map params);

    Integer count(Map params);

    void create(GasFilling gasFilling);

    GasFilling merge( String stationNumber,Integer machineNumber,String cynNumber);

    void deleteById(Integer id);

}

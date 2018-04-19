package com.donno.nj.service;

import com.donno.nj.domain.GasCyrChargeSpec;
import com.donno.nj.domain.Ticket;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
public interface GasCyrChargeSpecService
{
    List<GasCyrChargeSpec> retrieve(Map params);

    Integer count(Map params);

    void create(GasCyrChargeSpec gasCyrChargeSpec);

    void update(Integer id, GasCyrChargeSpec newGasCyrChargeSpec);

    void deleteById(Integer id);
}

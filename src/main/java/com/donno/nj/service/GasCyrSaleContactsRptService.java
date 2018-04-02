package com.donno.nj.service;

import com.donno.nj.domain.GasCyrSaleContactsRpt;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import com.google.common.base.Optional;

@Transactional
public interface GasCyrSaleContactsRptService
{
    List<GasCyrSaleContactsRpt> retrieveDailyRpt(Map params);

   Integer countDailyRpt(Map params);

    void create(GasCyrSaleContactsRpt gasCyrSaleContactsRpt);

    void update(Integer id, GasCyrSaleContactsRpt gasCyrSaleContactsRpt);

    void deleteById(Integer id);

    Optional<GasCyrSaleContactsRpt > findById(Integer id);
}

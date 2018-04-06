package com.donno.nj.service;


import com.donno.nj.domain.SettlementType;
import com.google.common.base.Optional;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
public interface SettlementTypeService
{
    Optional<SettlementType> findByCode(String code);

    List<SettlementType> retrieve(Map params);

    Integer count(Map params);

    SettlementType create(SettlementType settlementType);

    void update(Integer id, SettlementType newSettlementType);

    void delete(Integer id);
}

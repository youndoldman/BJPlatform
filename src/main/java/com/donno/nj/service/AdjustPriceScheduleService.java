package com.donno.nj.service;

import com.donno.nj.domain.AdjustPriceSchedule;
import com.donno.nj.domain.Order;
import com.google.common.base.Optional;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
public interface AdjustPriceScheduleService
{
    Optional<AdjustPriceSchedule> findByName(String name);

    Optional<AdjustPriceSchedule> findById(Integer id);

    List<AdjustPriceSchedule> retrieve(Map params);

    Integer count(Map params);

    void create(AdjustPriceSchedule adjustPriceSchedule);

    void update(Integer id, AdjustPriceSchedule newAdjustPriceSchedule);

    void deleteById(Integer id);
}

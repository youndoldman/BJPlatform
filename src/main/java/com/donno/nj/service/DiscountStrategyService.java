package com.donno.nj.service;

import com.donno.nj.domain.AdjustPriceSchedule;
import com.donno.nj.domain.DiscountStrategy;
import com.google.common.base.Optional;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
public interface DiscountStrategyService
{
    Optional<DiscountStrategy> findByName(String name);

    Optional<DiscountStrategy> findById(Integer id);

    List<DiscountStrategy> retrieve(Map params);

    Integer count(Map params);

    void create(DiscountStrategy discountStrategy);

    void update(Integer id, DiscountStrategy newDiscountStrategy);

    void deleteById(Integer id);

}

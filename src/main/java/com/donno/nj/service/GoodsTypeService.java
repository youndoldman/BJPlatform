package com.donno.nj.service;

import com.donno.nj.domain.GoodsType;
import com.google.common.base.Optional;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
public interface GoodsTypeService
{
    Optional<GoodsType> findByCode(String code);

    List<GoodsType> retrieve(Map params);

    Integer count(Map params);

    GoodsType create(GoodsType customerType);

    void update(String code, GoodsType newCustomerType);

    void deleteByCode(String code);
}

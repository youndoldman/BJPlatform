package com.donno.nj.service;

import com.donno.nj.domain.MendType;
import com.google.common.base.Optional;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
public interface MendTypeService
{
    Optional<MendType> findByCode(String code);

    List<MendType> retrieve(Map params);

    Integer count(Map params);

    MendType create(MendType customerType);

    void update(String code, MendType newCustomerType);

    void deleteByCode(String code);
}

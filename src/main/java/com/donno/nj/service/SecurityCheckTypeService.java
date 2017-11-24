package com.donno.nj.service;

import com.donno.nj.domain.SecurityCheckType;
import com.google.common.base.Optional;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
public interface SecurityCheckTypeService
{
    Optional<SecurityCheckType> findByCode(String code);

    List<SecurityCheckType> retrieve(Map params);

    Integer count(Map params);

    SecurityCheckType create(SecurityCheckType customerType);

    void update(Integer id, SecurityCheckType newCustomerType);

    void deleteById(Integer id);
}

package com.donno.nj.service;

import com.donno.nj.domain.SecurityType;
import com.google.common.base.Optional;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
public interface SecurityTypeService
{
    Optional<SecurityType> findByCode(String code);

    List<SecurityType> retrieve(Map params);

    Integer count(Map params);

    SecurityType create(SecurityType securityType);

    void update(String code, SecurityType newSecurityType);

    void deleteByCode(String code);
}

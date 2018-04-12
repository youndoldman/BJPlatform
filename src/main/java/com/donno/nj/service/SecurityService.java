package com.donno.nj.service;

import com.donno.nj.domain.Security;
import com.google.common.base.Optional;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
public interface SecurityService
{
    Optional<Security> findBySn(String sn);

    List<Security> retrieve(Map params);

    Integer count(Map params);

    void create(Security security);

    void update(String sn, Security newSecurity);

    void deleteById(Integer id);
}

package com.donno.nj.service;

import com.donno.nj.domain.GasCylinderSpec;
import com.donno.nj.domain.GasCynFactory;
import com.google.common.base.Optional;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
public interface GasCynFactoryService
{

    Optional<GasCynFactory> findByCode(String code);

    List<GasCynFactory> retrieve(Map params);

    Integer count(Map params);

    GasCynFactory create(GasCynFactory gasCynFactory);

    void update(String code, GasCynFactory newGasCynFactory);

    void deleteByCode(String code);
}
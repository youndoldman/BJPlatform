package com.donno.nj.service;

import com.donno.nj.domain.GasCylinderSpec;
import com.google.common.base.Optional;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
public interface GasCylinderSpecService
{

    Optional<GasCylinderSpec> findByCode(String code);

    List<GasCylinderSpec> retrieve(Map params);

    Integer count(Map params);

    GasCylinderSpec create(GasCylinderSpec gasCylinderSpec);

    void update(String code, GasCylinderSpec newGasCylinderSpec);

    void deleteByCode(String code);
}
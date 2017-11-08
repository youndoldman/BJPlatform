package com.donno.nj.service;

import com.donno.nj.domain.GasStore;
import com.google.common.base.Optional;

import java.util.List;
import java.util.Map;

/**
 * Created by T470P on 2017/10/30.
 */
public interface GasStoreService
{
    Optional<GasStore> findByCode(String code);

    List<GasStore> retrieve(Map params);

    Integer count(Map params);

    GasStore create(GasStore gasStore);

    void update(GasStore gasStore, GasStore newGasStore);

    void delete(GasStore gasStore);
}

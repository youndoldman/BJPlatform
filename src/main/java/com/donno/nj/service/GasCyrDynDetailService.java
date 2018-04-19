package com.donno.nj.service;

import com.donno.nj.domain.GasCyrChargeSpec;
import com.donno.nj.domain.GasCyrDynDetail;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
public interface GasCyrDynDetailService
{
    List<GasCyrDynDetail> retrieve(Map params);

    Integer count(Map params);

    void create(GasCyrDynDetail gasCyrDynDetail);

    void update(Integer id, GasCyrDynDetail newGasCyrDynDetail);

    void deleteById(Integer id);
}

package com.donno.nj.service;

import com.donno.nj.domain.GasCylinder;
import com.donno.nj.domain.GasCylinderSvcStatusOpHis;
import com.google.common.base.Optional;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
public interface GasCylinderSvcOpHisService
{
    List<GasCylinderSvcStatusOpHis> retrieve(Map params);

    Integer count(Map params);
}
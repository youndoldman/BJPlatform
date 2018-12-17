package com.donno.nj.service;

import com.donno.nj.domain.SalesRptByWeight;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
public interface RefoundByWeightRptService
{
    List<SalesRptByWeight> retrieveRefoundRpt(Map params);

}

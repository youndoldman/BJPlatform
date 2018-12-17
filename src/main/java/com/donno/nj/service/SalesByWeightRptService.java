package com.donno.nj.service;

import com.donno.nj.domain.EByType;
import com.donno.nj.domain.SalesRptByWeight;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
public interface SalesByWeightRptService
{
    List<SalesRptByWeight> retrieveSaleRpt(Map params);

}

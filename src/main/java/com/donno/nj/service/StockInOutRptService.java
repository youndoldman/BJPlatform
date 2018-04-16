package com.donno.nj.service;


import com.donno.nj.domain.StockRpt;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
public interface StockInOutRptService
{
    List<StockRpt> retrieve(Map params);

    Integer count(Map params);

}

package com.donno.nj.service;

import com.donno.nj.domain.DiscountStrategy;
import com.donno.nj.domain.GasCyrDailyStockRpt;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Transactional
public interface GasCyrDailyStockRptService
{
    List<GasCyrDailyStockRpt> retrieve(Map params);

   Integer count(Map params);
}

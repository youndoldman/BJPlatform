package com.donno.nj.service;

import com.donno.nj.domain.SalesRpt;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
public interface SalesRptService
{
    List<SalesRpt> retrieveDailyRpt(Map params);

    Integer countDailyRpt(Map params);
}

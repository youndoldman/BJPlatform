package com.donno.nj.service;

import com.donno.nj.domain.SaleContactsRpt;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Transactional
public interface SaleContactsCreditRptService
{
    List<SaleContactsRpt> retrieve(Map params);

    Integer count(Map params);
}

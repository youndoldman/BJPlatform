package com.donno.nj.service;

import com.donno.nj.domain.EByType;
import com.donno.nj.domain.SalesRpt;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
public interface SalesRptService
{
    List<SalesRpt> retrieveSaleRpt(Map params, EByType eByType);

//    Integer countSaleRptByPayType(Map params);
//
//    List<SalesRpt> retrieveSaleRptByCstType(Map params) ;
//
//    Integer countSaleRptByCstType(Map params);
}

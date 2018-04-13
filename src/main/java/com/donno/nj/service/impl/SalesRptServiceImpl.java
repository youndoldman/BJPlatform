package com.donno.nj.service.impl;

import com.donno.nj.dao.SalesByCstTypeRptDao;
import com.donno.nj.dao.SalesByPayTypeRptDao;
import com.donno.nj.domain.SalesRpt;

import com.donno.nj.service.SalesRptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class SalesRptServiceImpl implements SalesRptService
{
    @Autowired
    private SalesByPayTypeRptDao salesByPayTypeRptDao;

    @Autowired
    private SalesByCstTypeRptDao salesByCstTypeRptDao;

    @Override
    public List<SalesRpt> retrieveSaleRptByPayType(Map params) {
        return salesByPayTypeRptDao.getSaleRptByPayType(params);
    }

    @Override
    public Integer countSaleRptByPayType(Map params) {
        return salesByPayTypeRptDao.countSaleRptByPayType(params);
    }


    @Override
    public List<SalesRpt> retrieveSaleRptByCstType(Map params) {
        return salesByCstTypeRptDao.getSaleRptByCstType(params);
    }

    @Override
    public Integer countSaleRptByCstType(Map params) {
        return salesByCstTypeRptDao.countSaleRptByCstType(params);
    }
}

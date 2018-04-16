package com.donno.nj.service.impl;

import com.donno.nj.dao.SalesByCstTypeRptDao;
import com.donno.nj.dao.SalesByPayTypeRptDao;
import com.donno.nj.dao.StockRptDao;
import com.donno.nj.domain.PayType;
import com.donno.nj.domain.SalesRpt;
import com.donno.nj.domain.StockRpt;
import com.donno.nj.service.SalesRptService;
import com.donno.nj.service.StockRptService;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class StockRptServiceImpl implements StockRptService
{
    @Autowired
    private StockRptDao stockRptDao;

    @Override
    public List<StockRpt> retrieve(Map params)
    {
        List<StockRpt> stockRpts = stockRptDao.getList(params);
        return stockRpts;
    }

    @Override
    public Integer count(Map params) {
        return stockRptDao.count(params);
    }


}

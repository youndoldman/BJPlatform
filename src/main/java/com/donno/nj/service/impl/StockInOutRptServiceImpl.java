package com.donno.nj.service.impl;

import com.donno.nj.dao.StockInOutRptDao;
import com.donno.nj.dao.StockRptDao;
import com.donno.nj.domain.StockRpt;
import com.donno.nj.service.StockInOutRptService;
import com.donno.nj.service.StockRptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class StockInOutRptServiceImpl implements StockInOutRptService
{
    @Autowired
    private StockInOutRptDao stockInOutRptDao;

    @Override
    public List<StockRpt> retrieve(Map params)
    {
        List<StockRpt> stockRpts = stockInOutRptDao.getList(params);
        return stockRpts;
    }

    @Override
    public Integer count(Map params) {
        return stockInOutRptDao.count(params);
    }


}

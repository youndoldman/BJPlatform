package com.donno.nj.service.impl;

import com.donno.nj.dao.SalesRptDao;
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
    private SalesRptDao salesRptDao;

    @Override
    public List<SalesRpt> retrieveDailyRpt(Map params) {
        return salesRptDao.getDailyRpt(params);
    }

    @Override
    public Integer countDailyRpt(Map params) {
        return salesRptDao.countDailyRpt(params);
    }

}

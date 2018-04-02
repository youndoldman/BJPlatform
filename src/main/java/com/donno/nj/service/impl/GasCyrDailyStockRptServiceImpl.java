package com.donno.nj.service.impl;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.dao.GasCylinderStockInOutStaticsDao;
import com.donno.nj.dao.GasCylinderStockStaticsDao;
import com.donno.nj.dao.GasCyrDailyStockRptDao;
import com.donno.nj.domain.*;
import com.donno.nj.service.GasCyrDailyStockRptService;
import com.donno.nj.util.Clock;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class GasCyrDailyStockRptServiceImpl implements GasCyrDailyStockRptService
{

    @Autowired
    private GasCyrDailyStockRptDao gasCyrDailyStockRptDao;


    @Override
    public List<GasCyrDailyStockRpt> retrieve(Map params) {
        return gasCyrDailyStockRptDao.getList(params);
    }

    @Override
    public Integer count(Map params) {
        return gasCyrDailyStockRptDao.count(params);
    }

}

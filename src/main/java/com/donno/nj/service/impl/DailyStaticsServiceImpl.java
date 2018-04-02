package com.donno.nj.service.impl;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.dao.GasCylinderStockInOutStaticsDao;
import com.donno.nj.dao.GasCylinderStockStaticsDao;
import com.donno.nj.dao.GasCyrDailyStockRptDao;
import com.donno.nj.domain.*;
import com.donno.nj.service.DailyStaticsService;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.donno.nj.util.Clock;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Service
public class DailyStaticsServiceImpl implements DailyStaticsService
{

    @Autowired
    private GasCylinderStockInOutStaticsDao gasCylinderStockInOutStaticsDao;

    @Autowired
    private GasCyrDailyStockRptDao gasCyrDailyStockRptDao;

    @Autowired
    private GasCylinderStockStaticsDao gasCylinderStockStaticsDao;

    @Override
    @OperationLog(desc = "日常统计任务")
    public void dailyStatics() throws ParseException
    {
        gasCysDailyStatics();

    }

    public void gasCysDailyStatics() throws ParseException
    {
        gasCysDailyInOutStatics();
        gasCysDailyStockStatics();
        gasCysMonthStockInStatics();
    }

    /*钢瓶出入库*/
    public void gasCysDailyInOutStatics() throws ParseException
    {
        Date startTime = Clock.getLastDay(new Date());
        Date endTime = new Date();

        Map params = new HashMap<String,String>();
        params.putAll(ImmutableMap.of("startTime", startTime));
        params.putAll(ImmutableMap.of("endTime", endTime));

        List<GasCylinderStockInOutStatics> gasCylinderStockInOutStaticsList = gasCylinderStockInOutStaticsDao.getStatics(params);

        for(GasCylinderStockInOutStatics gasCylinderStockInOutStatics :gasCylinderStockInOutStaticsList)
        {
            Department department = gasCylinderStockInOutStatics.getDepartment();
            GasCylinderSpec gasCylinderSpec = gasCylinderStockInOutStatics.gasCylinderSpec();

            GasCyrDailyStockRpt gasCyrDailyStockRpt = gasCyrDailyStockRptDao.findByDaily(department.getCode(),gasCylinderSpec.getCode(),endTime);
            if (gasCyrDailyStockRpt == null)
            {
                gasCyrDailyStockRpt = new GasCyrDailyStockRpt();
                gasCyrDailyStockRpt.setDepartment(department);
                gasCyrDailyStockRpt.setGasCylinderSpec(gasCylinderSpec);

                if (gasCylinderStockInOutStatics.getStockType() == StockType.STStockIn)
                {
                    gasCyrDailyStockRpt.setStockInAmount(gasCylinderStockInOutStatics.getAmount());
                }
                else if (gasCylinderStockInOutStatics.getStockType() == StockType.STStockOut)
                {
                    gasCyrDailyStockRpt.setStockOutAmount(gasCylinderStockInOutStatics.getAmount());
                }

                gasCyrDailyStockRpt.setDate(endTime);
                gasCyrDailyStockRptDao.insert(gasCyrDailyStockRpt);
            }
            else
            {
                if (gasCylinderStockInOutStatics.getStockType() == StockType.STStockIn)
                {
                    gasCyrDailyStockRpt.setStockInAmount(gasCylinderStockInOutStatics.getAmount());
                }
                else if (gasCylinderStockInOutStatics.getStockType() == StockType.STStockOut)
                {
                    gasCyrDailyStockRpt.setStockOutAmount(gasCylinderStockInOutStatics.getAmount());
                }
                gasCyrDailyStockRptDao.update(gasCyrDailyStockRpt);
            }
        }
    }

    /*钢瓶库存*/
    public void gasCysDailyStockStatics()
    {
        List<GasCylinderStockStatics> gasCylinderStockStaticsList = gasCylinderStockStaticsDao.getStatics(GasCynServiceStatus.StoreStock.getIndex());

        for(GasCylinderStockStatics gasCylinderStockStatics :gasCylinderStockStaticsList)
        {
            Department department = gasCylinderStockStatics.getDepartment();
            GasCylinderSpec gasCylinderSpec = gasCylinderStockStatics.gasCylinderSpec();

            Date date = new Date();
            GasCyrDailyStockRpt gasCyrDailyStockRpt = gasCyrDailyStockRptDao.findByDaily(department.getCode(),gasCylinderSpec.getCode(),date);
            if (gasCyrDailyStockRpt == null)
            {
                gasCyrDailyStockRpt = new GasCyrDailyStockRpt();
                gasCyrDailyStockRpt.setDepartment(department);
                gasCyrDailyStockRpt.setGasCylinderSpec(gasCylinderSpec);
                gasCyrDailyStockRpt.setStockAmount(gasCylinderStockStatics.getAmount());
                gasCyrDailyStockRptDao.insert(gasCyrDailyStockRpt);
            }
            else
            {
                gasCyrDailyStockRpt.setDepartment(department);
                gasCyrDailyStockRpt.setGasCylinderSpec(gasCylinderSpec);
                gasCyrDailyStockRpt.setStockAmount(gasCylinderStockStatics.getAmount());
                gasCyrDailyStockRptDao.update(gasCyrDailyStockRpt);
            }
        }
    }

    /*截止当天当月累计入库数量*/
    public void gasCysMonthStockInStatics()
    {
        Date startTime = Clock.getStartDayOfMonth();
        Date endTime = new Date();

        Map params = new HashMap<String,String>();
        params.putAll(ImmutableMap.of("startTime", startTime));
        params.putAll(ImmutableMap.of("endTime", endTime));
        params.putAll(ImmutableMap.of("stockType", StockType.STStockIn));

        List<GasCylinderStockInOutStatics> gasCylinderStockInOutStaticsList = gasCylinderStockInOutStaticsDao.getStatics(params);

        for(GasCylinderStockInOutStatics gasCylinderStockInOutStatics :gasCylinderStockInOutStaticsList)
        {
            Department department = gasCylinderStockInOutStatics.getDepartment();
            GasCylinderSpec gasCylinderSpec = gasCylinderStockInOutStatics.gasCylinderSpec();

            GasCyrDailyStockRpt gasCyrDailyStockRpt = gasCyrDailyStockRptDao.findByDaily(department.getCode(),gasCylinderSpec.getCode(),endTime);
            if (gasCyrDailyStockRpt == null)
            {
                gasCyrDailyStockRpt = new GasCyrDailyStockRpt();
                gasCyrDailyStockRpt.setDepartment(department);
                gasCyrDailyStockRpt.setGasCylinderSpec(gasCylinderSpec);

                gasCyrDailyStockRpt.setMonthStockInAmount(gasCylinderStockInOutStatics.getAmount());
                gasCyrDailyStockRpt.setDate(endTime);
                gasCyrDailyStockRptDao.insert(gasCyrDailyStockRpt);
            }
            else
            {
                gasCyrDailyStockRpt.setMonthStockInAmount(gasCylinderStockInOutStatics.getAmount());
                gasCyrDailyStockRptDao.update(gasCyrDailyStockRpt);
            }
        }
    }

}

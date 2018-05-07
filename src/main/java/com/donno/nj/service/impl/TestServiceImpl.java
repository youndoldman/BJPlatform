package com.donno.nj.service.impl;

import com.donno.nj.dao.GasCylinderDao;
import com.donno.nj.dao.GasCylinderSpecDao;
import com.donno.nj.dao.SysUserDao;
import com.donno.nj.dao.UserDao;
import com.donno.nj.domain.*;
import com.donno.nj.service.GasCylinderService;
import com.donno.nj.service.TestService;

import com.donno.nj.service.UserService;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Service
public class TestServiceImpl implements TestService
{
    @Autowired
    private GasCylinderDao gasCylinderDao;

    @Autowired
    private GasCylinderSpecDao gasCylinderSpecDao;

    @Override
    public void run()
    {
        try
        {
            // AddGasCylinder();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    void AddGasCylinder()
    {
        Integer targetCount = 500000;
        Map params = new HashMap<String,String>();
        List<GasCylinderSpec> gasCylinderSpecList = gasCylinderSpecDao.getList(params);

        for(GasCylinderSpec gasCylinderSpec :gasCylinderSpecList)
        {
            params.clear();
            params.putAll(ImmutableMap.of("specCode", gasCylinderSpec.getCode()));
            Integer startCount = gasCylinderDao.count(params);

            Integer count = startCount;
            while(true)
            {
                count++;

                GasCylinder gasCylinder = new GasCylinder();
                String number = String.format("1100%d",count);
                gasCylinder.setNumber(number);
                gasCylinder.setSpec(gasCylinderSpec);
                gasCylinder.setServiceStatus(GasCynServiceStatus.StationStock);
                gasCylinder.setLifeStatus(DeviceStatus.DevEnabled);
                gasCylinder.setLoadStatus(LoadStatus.LSHeavy);
                gasCylinder.setNextVerifyDate(new Date());
                gasCylinder.setProductionDate(new Date());
                gasCylinder.setVerifyDate(new Date());
                gasCylinder.setScrapDate(new Date());
   
                gasCylinder.setNote("test");

                gasCylinderDao.insert(gasCylinder);
                if (count - startCount > targetCount)
                {
                    break;
                }
            }

        }
    }

}

package com.donno.nj.service.impl;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.dao.*;
import com.donno.nj.domain.*;

import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.service.GasCylinderWarnService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Map;


@Service
public class GasCylinderWarnServiceImpl implements GasCylinderWarnService
{

    @Autowired
    private GasCynForceTakeOverWarnDao gasCynForceTakeOverWarnDao;


    @Override
    @OperationLog(desc = "查询钢瓶告警信息")
    public List<GasCynForceTakeOverWarn> retrieve(Map params)
    {
        List<GasCynForceTakeOverWarn> gasCynForceTakeOverWarnList = gasCynForceTakeOverWarnDao.getList(params);
        return gasCynForceTakeOverWarnList;
    }

    @Override
    @OperationLog(desc = "查询钢瓶告警数量")
    public Integer count(Map params) {
        return gasCynForceTakeOverWarnDao.count(params);
    }


    @Override
    @OperationLog(desc = "查询钢瓶告警数量")
    public void update(Integer id, GasCynForceTakeOverWarn newGasCynForceTakeOverWarn)
    {
        //查找记录是否存在
        GasCynForceTakeOverWarn gasCynForceTakeOverWarn = gasCynForceTakeOverWarnDao.findById(id);
        if ( gasCynForceTakeOverWarn == null)
        {
            throw new ServerSideBusinessException("未查找到该告警记录！", HttpStatus.NOT_ACCEPTABLE);
        }

        if (newGasCynForceTakeOverWarn.getGasCynWarnStatus().getIndex() < GasCynWarnStatus.Created.getIndex() ||
                newGasCynForceTakeOverWarn.getGasCynWarnStatus().getIndex() > GasCynWarnStatus.Processed.getIndex())
        {
            throw new ServerSideBusinessException("状态值错误！", HttpStatus.NOT_ACCEPTABLE);
        }

        newGasCynForceTakeOverWarn.setId(id);
        gasCynForceTakeOverWarnDao.update(newGasCynForceTakeOverWarn);
    }
}

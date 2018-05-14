package com.donno.nj.service.impl;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.dao.*;
import com.donno.nj.domain.*;
import com.donno.nj.service.GasCylinderSvcOpHisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;


@Service
public class GasCylinderSvcOpHisServiceImpl implements GasCylinderSvcOpHisService
{
    @Autowired
    GasCylinderSvcStatusOpHisDao gasCylinderSvcStatusOpHisDao;

    @Override
    @OperationLog(desc = "查询钢瓶操作历史信息")
    public List<GasCylinderSvcStatusOpHis> retrieve(Map params)
    {
        List<GasCylinderSvcStatusOpHis> gasCylinderSvcStatusOpHises = gasCylinderSvcStatusOpHisDao.getList(params);
        return gasCylinderSvcStatusOpHises;
    }

    @Override
    @OperationLog(desc = "查询钢瓶操作历史数量")
    public Integer count(Map params) {
        return gasCylinderSvcStatusOpHisDao.count(params);
    }
}

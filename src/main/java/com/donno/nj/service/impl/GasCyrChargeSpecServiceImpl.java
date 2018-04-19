package com.donno.nj.service.impl;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.dao.*;
import com.donno.nj.domain.*;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.service.GasCyrChargeSpecService;
import com.donno.nj.service.TicketService;
import com.donno.nj.util.Clock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class GasCyrChargeSpecServiceImpl implements GasCyrChargeSpecService
{
    @Autowired
    private GasCyrChargeSpecDao gasCyrChargeSpecDao;

    @Autowired
    private GasCylinderSpecDao gasCylinderSpecDao;

    @Override
    @OperationLog(desc = "查询钢瓶收费标准信息")
    public List<GasCyrChargeSpec> retrieve(Map params)
    {
        List<GasCyrChargeSpec> gasCyrChargeSpecs = gasCyrChargeSpecDao.getList(params);
        return gasCyrChargeSpecs;
    }

    @Override
    @OperationLog(desc = "查询钢瓶收费标准信息数量")
    public Integer count(Map params) {
        return gasCyrChargeSpecDao.count(params);
    }


    public void checkGasCyrSpec(String specCode)
    {
        if (specCode == null || specCode.trim().length() == 0)
        {
            throw new ServerSideBusinessException("缺少钢瓶规格信息！", HttpStatus.NOT_ACCEPTABLE);
        }

        GasCylinderSpec  target = gasCylinderSpecDao.findByCode(specCode);
        if (target == null)
        {
            throw new ServerSideBusinessException("钢瓶规格信息不存在！", HttpStatus.NOT_ACCEPTABLE);
        }
    }



    @Override
    @OperationLog(desc = "添加钢瓶收费标准信息")
    public void create(GasCyrChargeSpec gasCyrChargeSpec)
    {
        /*参数校验*/
        if (gasCyrChargeSpec == null)
        {
            throw new ServerSideBusinessException("钢瓶收费标准信息不全，请补充信息！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*规格检查*/
        checkGasCyrSpec(gasCyrChargeSpec.getGasCyrSpecCode()) ;

        gasCyrChargeSpecDao.insert(gasCyrChargeSpec);
    }

    @Override
    @OperationLog(desc = "修改钢瓶收费标准信息")
    public void update(Integer id, GasCyrChargeSpec newGasCyrChargeSpec)
    {
        /*参数校验*/
        if (newGasCyrChargeSpec == null )
        {
            throw new ServerSideBusinessException("钢瓶收费标准信息不能为空！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*是否存在*/
        GasCyrChargeSpec gasCyrChargeSpec = gasCyrChargeSpecDao.findById(id);
        if (gasCyrChargeSpec == null)
        {
            throw new ServerSideBusinessException("钢瓶收费标准信息不存在！", HttpStatus.NOT_ACCEPTABLE);
        }


        /*更新数据*/
        newGasCyrChargeSpec.setId(gasCyrChargeSpec.getId());
        gasCyrChargeSpecDao.update(newGasCyrChargeSpec);
    }



    @Override
    @OperationLog(desc = "删除钢瓶收费标准信息")
    public void deleteById(Integer id)
    {
        GasCyrChargeSpec gasCyrChargeSpec = gasCyrChargeSpecDao.findById(id);
        if (gasCyrChargeSpec == null)
        {
            throw new ServerSideBusinessException("钢瓶收费标准息不存在！", HttpStatus.NOT_FOUND);
        }

        gasCyrChargeSpecDao.delete(id);
    }

}

package com.donno.nj.service.impl;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.dao.*;
import com.donno.nj.domain.*;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.service.GasFillingService;
import com.donno.nj.util.AppUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GasFillingServiceImpl implements GasFillingService
{
    @Autowired
    private GasFillingDao gasFillingDao;

    @Autowired
    private GasCylinderDao gasCylinderDao;

    @Autowired
    private    SystemParamDao systemParamDao;

    @Autowired
    private     GasFillingMergeDao gasFillingMergeDao;

    @Override
    @OperationLog(desc = "查询充装信息")
    public List<GasFilling> retrieve(Map params)
    {
        List<GasFilling> fillingList = gasFillingMergeDao.getList(params);

        return fillingList;
    }

    @Override
    @OperationLog(desc = "查询充装数量")
    public Integer count(Map params) {
        return gasFillingMergeDao.count(params);
    }


    @Override
    @OperationLog(desc = "添加充装信息")
    public void create(GasFilling gasFilling)
    {
        /*参数校验*/
        if (gasFilling == null)
        {
            throw new ServerSideBusinessException("充装信息不全，请补充信息！", HttpStatus.NOT_ACCEPTABLE);
        }

        if (gasFilling.getStationNumber() == null || gasFilling.getStationNumber().trim().length() == 0)
        {
            throw new ServerSideBusinessException("缺少气站编号信息，请补充信息！", HttpStatus.NOT_ACCEPTABLE);
        }

        if (gasFilling.getMachineNumber() == null)
        {
            throw new ServerSideBusinessException("缺少充装秤编号信息，请补充信息！", HttpStatus.NOT_ACCEPTABLE);
        }


        GasFilling target = gasFillingDao.find(gasFilling.getStationNumber(),gasFilling.getMachineNumber());
        if (target != null)
        {

            if (target.getSequence() == gasFilling.getSequence())//流水号一样为重复数据
            {
                //流水号一样为重复数据,忽略
            }
            else
            {
                //已经有数据，覆盖旧数据
                gasFilling.setId(target.getId());
                gasFillingDao.update(gasFilling);
            }
        }
        else
        {
            gasFillingDao.insert(gasFilling);
        }
    }


    public GasCylinder checkGasCyn(String gasCynNumber)
    {
        if (gasCynNumber == null || gasCynNumber.trim().length() == 0)
        {
            throw new ServerSideBusinessException("缺少钢瓶编号！", HttpStatus.NOT_ACCEPTABLE);
        }

        GasCylinder gasCylinder = gasCylinderDao.findByNumber(gasCynNumber);
        if (gasCylinder == null)
        {
            String message = String.format("钢瓶编号%s不存在",gasCynNumber);
            throw new ServerSideBusinessException(message, HttpStatus.NOT_ACCEPTABLE);
        }

        return gasCylinder;
    }

    @Override
    @OperationLog(desc = "充装信息融合")
    public GasFilling merge(  String stationNumber,Integer machineNumber,String cynNumber)
    {
        /*参数校验*/
        if (stationNumber == null )
        {
            throw new ServerSideBusinessException("站号信息不能为空！", HttpStatus.NOT_ACCEPTABLE);
        }

        if (machineNumber == null )
        {
            throw new ServerSideBusinessException("秤号信息不能为空！", HttpStatus.NOT_ACCEPTABLE);
        }

        if (cynNumber == null || cynNumber.trim().length() == 0)
        {
            throw new ServerSideBusinessException("钢瓶编号不能为空！", HttpStatus.NOT_ACCEPTABLE);
        }

        GasCylinder gasCylinder = checkGasCyn(cynNumber);

        GasFilling target = gasFillingDao.find(stationNumber,machineNumber);
        if (target != null)
        {
            target.setCynNumber(cynNumber);
            target.setMergeTime(new Date());

            com.google.common.base.Optional<User> userOptional =  AppUtil.getCurrentLoginUser();
//            if (!userOptional.isPresent())
//            {
//                throw new ServerSideBusinessException("未读取到操作员信息，请重新登录！", HttpStatus.NOT_ACCEPTABLE);
//            }
//            target.setUserId(userOptional.get().getUserId());
            target.setUserId("admin");

            //告警判断
            target.setWarnningStatus(WarnningStatus.WSNormal);
            if (gasCylinder.getEmptyWeight() != null &&  gasCylinder.getEmptyWeight() > 0)
            {
                Float differWeight = systemParamDao.getGasTareDfferWeight();

                if (gasCylinder.getEmptyWeight() - target.getTareWeight()  > differWeight )
                {
                    target.setWarnningStatus(WarnningStatus.WSWarnning1);
                }
            }

            /*添加到融合后的数据表*/
            gasFillingMergeDao.insert(target);

            /*删除当前记录*/
            gasFillingDao.delete(target.getId());
        }
        else
        {
            throw new ServerSideBusinessException("没有充装数据！", HttpStatus.NOT_FOUND);
        }

        return target;
    }

    @Override
    @OperationLog(desc = "删除充装信息")
    public void deleteById(Integer id)
    {
        GasFilling gasFilling = gasFillingDao.findById(id);
        if (gasFilling == null)
        {
            throw new ServerSideBusinessException("充装信息不存在！", HttpStatus.NOT_FOUND);
        }

        gasFillingDao.delete(id);
    }

}

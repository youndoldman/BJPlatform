package com.donno.nj.service.impl;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.dao.GasCylinderBindRelationDao;
import com.donno.nj.dao.GasCylinderDao;
import com.donno.nj.dao.GasCylinderSpecDao;
import com.donno.nj.dao.LocationDeviceDao;
import com.donno.nj.domain.DeviceStatus;
import com.donno.nj.domain.GasCylinder;
import com.donno.nj.domain.GasCylinderSpec;
import com.donno.nj.domain.LocationDevice;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.service.GasCylinderService;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class GasCylinderServiceImpl implements GasCylinderService
{

    @Autowired
    private GasCylinderDao gasCylinderDao;

    @Autowired
    private GasCylinderSpecDao gasCylinderSpecDao;

    @Autowired
    private  LocationDeviceDao locationDeviceDao;

    @Autowired
    private GasCylinderBindRelationDao gasCylinderBindRelationDao;

    @Override
    public Optional<GasCylinder> findByNumber(String number)
    {
        return Optional.fromNullable(gasCylinderDao.findByNumber(number));
    }

    @Override
    @OperationLog(desc = "查询钢瓶信息")
    public List<GasCylinder> retrieve(Map params)
    {
        List<GasCylinder> gasCylinderList = gasCylinderDao.getList(params);
        return gasCylinderList;
    }

    @Override
    @OperationLog(desc = "查询钢瓶数量")
    public Integer count(Map params) {
        return gasCylinderDao.count(params);
    }

    @Override
    @OperationLog(desc = "创建钢瓶信息")
    public void create(GasCylinder gasCylinder)
    {
        /*参数校验*/
        if (gasCylinder == null || gasCylinder.getNumber() == null ||
                gasCylinder.getSpec() == null ||
                gasCylinder.getProductionDate() == null || gasCylinder.getVerifyDate() == null ||
                gasCylinder.getNextVerifyDate() == null || gasCylinder.getScrapDate() == null
                )
        {
            throw new ServerSideBusinessException("钢瓶信息不全，请补充钢瓶信息！", HttpStatus.NOT_ACCEPTABLE);
        }


        /*钢瓶规格信息校验*/
        if (gasCylinder.getSpec() != null )
        {
            GasCylinderSpec spec = gasCylinderSpecDao.findByCode(gasCylinder.getSpec().getCode());
            if (spec != null)
            {
                gasCylinder.setSpec(spec);
            }
            else
            {
                throw new ServerSideBusinessException("钢瓶规格信息错误！", HttpStatus.NOT_ACCEPTABLE);
            }
        }
        else
        {
            throw new ServerSideBusinessException("钢瓶规格不能为空！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*钢瓶是否已经存在*/
        if (findByNumber(gasCylinder.getNumber()).isPresent())
        {
            throw new ServerSideBusinessException("钢瓶信息已经存在！", HttpStatus.CONFLICT);
        }

        gasCylinderDao.insert(gasCylinder);
    }


    @Override
    @OperationLog(desc = "建立绑定关系")
    public void bindLocationDev(String gasCylinderNumber,String locationDeviceNumber)
    {
        /*参数校验*/
        if (gasCylinderNumber == null || gasCylinderNumber.trim().length() == 0 )
        {
            throw new ServerSideBusinessException("钢瓶信息不全，缺少钢瓶编号！", HttpStatus.NOT_ACCEPTABLE);
        }

        if (locationDeviceNumber == null || locationDeviceNumber.trim().length() == 0 )
        {
            throw new ServerSideBusinessException("定位终端信息不全，缺少定位终端编号！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*钢瓶是否存在*/
        Optional<GasCylinder> gasCylinderOptional = findByNumber(gasCylinderNumber);
        if ( !gasCylinderOptional.isPresent())
        {
            throw new ServerSideBusinessException("钢瓶信息不存在！", HttpStatus.NOT_FOUND);
        }
        GasCylinder gasCylinder = gasCylinderOptional.get();

        /*如果定位终端不存在，则创建定位终端信息*/
        LocationDevice locationDevice = locationDeviceDao.findByNumber(locationDeviceNumber);
        if (locationDevice == null)
        {
            locationDevice = new LocationDevice();
            locationDevice.setNumber(locationDeviceNumber);
            locationDevice.setStatus(DeviceStatus.DevEnabled);
            locationDeviceDao.insert(locationDevice);
        }

        /*如果钢瓶已经被绑定了，提示应先解绑定*/
        if ( gasCylinderBindRelationDao.findLocateDevByCylinderId(gasCylinder.getId()) != null)
        {
            throw new ServerSideBusinessException("钢瓶已经绑定，请先解绑定！", HttpStatus.CONFLICT);
        }

        /*如果定位终端已经被绑定了，提示应先解绑定*/
        if ( gasCylinderBindRelationDao.findGasCylinderByLocateDevId(locationDevice.getId()) != null)
        {

            throw new ServerSideBusinessException("定位终端已经绑定，请先解绑定！", HttpStatus.CONFLICT);
        }

        gasCylinderBindRelationDao.bindLocationDev(gasCylinder.getId(),locationDevice.getId());
    }

    @Override
    @OperationLog(desc = "解除绑定关系")
    public void unBindLocationDev(String gasCylinderNumber,String locationDeviceNumber)
    {
        /*参数校验*/
        if (gasCylinderNumber == null || gasCylinderNumber.trim().length() == 0 )
        {
            throw new ServerSideBusinessException("参数错误，缺少钢瓶编号！", HttpStatus.NOT_ACCEPTABLE);
        }

        if (locationDeviceNumber == null || locationDeviceNumber.trim().length() == 0 )
        {
            throw new ServerSideBusinessException("参数错误，缺少定位终端编号！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*钢瓶是否存在*/
        Optional<GasCylinder> gasCylinderOptional = findByNumber(gasCylinderNumber);
        if ( !gasCylinderOptional.isPresent())
        {
            throw new ServerSideBusinessException("钢瓶信息不存在！", HttpStatus.NOT_FOUND);
        }
        GasCylinder gasCylinder = gasCylinderOptional.get();


        /*定位终端不存在*/
        LocationDevice locationDevice = locationDeviceDao.findByNumber(locationDeviceNumber);
        if (locationDevice == null)
        {
            throw new ServerSideBusinessException("定位终端信息不存在！", HttpStatus.NOT_FOUND);
        }

        /*如果钢瓶和定位终端没有被绑定，提示错误信息*/
        if ( gasCylinderBindRelationDao.findBindRelation(gasCylinder.getId(),locationDevice.getId()) == null)
        {
            throw new ServerSideBusinessException("钢瓶和定位终端没有被绑定！", HttpStatus.NOT_FOUND);
        }

        gasCylinderBindRelationDao.unBindLocationDev(gasCylinder.getId(),locationDevice.getId());
    }


    @Override
    @OperationLog(desc = "修改钢瓶信息")
    public void update(String number, GasCylinder newGasCylinder)
    {
        /*参数校验*/
        if (number == null || number.trim().length() == 0 || newGasCylinder == null )
        {
            throw new ServerSideBusinessException("钢瓶信息不能为空！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*钢瓶是否存在*/
        Optional<GasCylinder> gasCylinderOptional = findByNumber(number);
        if (!gasCylinderOptional.isPresent())
        {
            throw new ServerSideBusinessException("钢瓶信息不存在！", HttpStatus.NOT_FOUND);
        }

        GasCylinder gasCylinder = findByNumber(number).get();
        newGasCylinder.setId(gasCylinder.getId());

        /*钢瓶新编号校验*/
        if (newGasCylinder.getNumber() != null && newGasCylinder.getNumber().trim().length() > 0 )
        {
            if (number.equals(newGasCylinder.getNumber()))//编号不修改
            {
                newGasCylinder.setNumber(null);
            }
            else
            {
                /*目标代码是否存在*/
                if (findByNumber(newGasCylinder.getNumber()).isPresent())
                {
                    throw new ServerSideBusinessException("钢瓶编号已经存在！", HttpStatus.CONFLICT);
                }
            }
        }
        else
        {
            newGasCylinder.setNumber(null);
        }

        /*钢瓶规格*/
        if (newGasCylinder.getSpec() != null && newGasCylinder.getSpec().getCode() != null && newGasCylinder.getSpec().getCode().trim().length() > 0)
        {
            GasCylinderSpec gasCylinderSpec =  gasCylinderSpecDao.findByCode(newGasCylinder.getSpec().getCode());
            if (gasCylinderSpec != null)
            {
                newGasCylinder.setSpec(gasCylinderSpec);
            }
            else
            {
                throw new ServerSideBusinessException("钢瓶规格信息不存在！", HttpStatus.NOT_ACCEPTABLE);
            }
        }
        else
        {
            newGasCylinder.setSpec(null);
        }

        /*更新数据*/
        gasCylinderDao.update(newGasCylinder);
    }

    @Override
    @OperationLog(desc = "删除商品信息")
    public void deleteById(Integer id)
    {
        GasCylinder gasCylinder = gasCylinderDao.findById(id);
        if (gasCylinder == null)
        {
            throw new ServerSideBusinessException("钢瓶不存在！",HttpStatus.NOT_FOUND);
        }

        /*有钢瓶使用记录，则不允许删除*/

        /*检查，如果有绑定关系，则解绑定*/
        LocationDevice locationDevice = gasCylinderBindRelationDao.findLocateDevByCylinderId(id);
        if (locationDevice != null)
        {
            gasCylinderBindRelationDao.unBindLocationDev(id,locationDevice.getId());
        }

        /*钢瓶位置信息表信息删除*/

        /*钢瓶轨迹信息表信息删除*/

        /*钢瓶基础信息表数据删除*/
        gasCylinderDao.deleteByIdx(id);
    }
}

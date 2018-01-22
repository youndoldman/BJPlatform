package com.donno.nj.service.impl;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.dao.*;
import com.donno.nj.domain.*;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.service.CloudUserService;
import com.donno.nj.service.GasCylinderService;
import com.donno.nj.util.AppUtil;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class CloudUserServiceImpl implements CloudUserService
{

//    @Autowired
//    private CloudUserDao cloudUserDao;
//
//
//    @Override
//    public Optional<CloudUser> findByCloudUserId(String cloudUserId)
//    {
//        return Optional.fromNullable(cloudUserDao.findByCloudUserId(cloudUserId));
//    }
//
//    @Override
//    @OperationLog(desc = "查询云客服信息")
//    public List<CloudUser> retrieve(Map params)
//    {
//        List<CloudUser> cloudUsers = cloudUserDao.getList(params);
//        return cloudUsers;
//    }
//
//    @Override
//    @OperationLog(desc = "查询云客服数量")
//    public Integer count(Map params) {
//        return cloudUserDao.count(params);
//    }
//
//    @Override
//    @OperationLog(desc = "创建云客服信息")
//    public void create(CloudUser cloudUser)
//    {
//        /*参数校验*/
//        if (cloudUser == null || cloudUser.getUserId() == null || cloudUser.getUserId().trim().length() == 0 )
//        {
//            throw new ServerSideBusinessException("云客服用户信息不全，缺少用户名称，请补充！", HttpStatus.NOT_ACCEPTABLE);
//        }
//
//        if (cloudUser.getPassword() == null || cloudUser.getPassword().trim().length() == 0)
//        {
//            throw new ServerSideBusinessException("云客服用户信息不全，缺少用户密码，请补充！", HttpStatus.NOT_ACCEPTABLE);
//        }
//
//
//        /*云客服用户是否已经存在*/
//        if (findByCloudUserId(cloudUser.getUserId()).isPresent())
//        {
//            throw new ServerSideBusinessException("云客服用户信息已经存在！", HttpStatus.CONFLICT);
//        }
//
//        cloudUserDao.insert(cloudUser);
//    }
//
//
//    @Override
//    @OperationLog(desc = "修改云客服信息")
//    public void update(String userId, CloudUser newCloudUser)
//    {
//        /*参数校验*/
//        if (userId == null || userId.trim().length() == 0 || newCloudUser == null )
//        {
//            throw new ServerSideBusinessException("云客服用户信息不能为空！", HttpStatus.NOT_ACCEPTABLE);
//        }
//
//        /*云客服用户是否存在*/
//        Optional<CloudUser> cloudUserOptional = findByCloudUserId(userId);
//        if (!cloudUserOptional.isPresent())
//        {
//            throw new ServerSideBusinessException("云客服用户信息不存在！", HttpStatus.NOT_FOUND);
//        }
//
//        CloudUser cloudUser = findByCloudUserId(userId).get();
//        newCloudUser.setId(cloudUser.getId());
//
//        /*云客服用户新ID校验*/
//        if (newCloudUser.getUserId() != null && newCloudUser.getUserId().trim().length() > 0 )
//        {
//            if (userId.equals(newCloudUser.getUserId()))//用户ID不修改
//            {
//                newCloudUser.getUserId(null);
//            }
//            else
//            {
//                /*目标用户是否存在*/
//                if (findByCloudUserId(newCloudUser.getUserId()).isPresent())
//                {
//                    throw new ServerSideBusinessException("云客服已经存在！", HttpStatus.CONFLICT);
//                }
//            }
//        }
//        else
//        {
//            newCloudUser.getUserId(null);
//        }
//
//
//
//        /*更新数据*/
//        cloudUserDao.update(newCloudUser);
//    }



//    @Override
//    @OperationLog(desc = "删除云客服信息")
//    public void deleteById(Integer id)
//    {
//        GasCylinder gasCylinder = gasCylinderDao.findById(id);
//        if (gasCylinder == null)
//        {
//            throw new ServerSideBusinessException("钢瓶不存在！",HttpStatus.NOT_FOUND);
//        }
//
//        /*有钢瓶使用记录，则不允许删除*/
//
//        /*检查，如果有绑定关系，则解绑定*/
//        LocationDevice locationDevice = gasCylinderBindRelationDao.findLocateDevByCylinderId(id);
//        if (locationDevice != null)
//        {
//            gasCylinderBindRelationDao.unBindLocationDev(id,locationDevice.getId());
//        }
//
//        /*钢瓶责任人关系表删除*/
//        GasCynUserRel gasCynUserRel = gasCynUserRelDao.findBindRel(id);
//        if (gasCynUserRel != null)
//        {
//            gasCynUserRelDao.delete(gasCynUserRel.getId());
//        }
//
//        /*钢瓶位置信息表信息删除*/
//
//        /*钢瓶轨迹信息表信息删除*/
//
//        /*钢瓶基础信息表数据删除*/
//        gasCylinderDao.deleteByIdx(id);
//    }

//    @Override
//    @OperationLog(desc = "建立绑定关系")
//    public void bindLocationDev(String gasCylinderNumber,String locationDeviceNumber)
//    {
//        /*参数校验*/
//        if (gasCylinderNumber == null || gasCylinderNumber.trim().length() == 0 )
//        {
//            throw new ServerSideBusinessException("钢瓶信息不全，缺少钢瓶编号！", HttpStatus.NOT_ACCEPTABLE);
//        }
//
//        if (locationDeviceNumber == null || locationDeviceNumber.trim().length() == 0 )
//        {
//            throw new ServerSideBusinessException("定位终端信息不全，缺少定位终端编号！", HttpStatus.NOT_ACCEPTABLE);
//        }
//
//        /*钢瓶是否存在*/
//        Optional<GasCylinder> gasCylinderOptional = findByNumber(gasCylinderNumber);
//        if ( !gasCylinderOptional.isPresent())
//        {
//            throw new ServerSideBusinessException("钢瓶信息不存在！", HttpStatus.NOT_FOUND);
//        }
//        GasCylinder gasCylinder = gasCylinderOptional.get();
//
//        /*如果定位终端不存在，则创建定位终端信息*/
//        LocationDevice locationDevice = locationDeviceDao.findByNumber(locationDeviceNumber);
//        if (locationDevice == null)
//        {
//            locationDevice = new LocationDevice();
//            locationDevice.setNumber(locationDeviceNumber);
//            locationDevice.setStatus(DeviceStatus.DevEnabled);
//            locationDeviceDao.insert(locationDevice);
//        }
//
//        /*如果钢瓶已经被绑定了，提示应先解绑定*/
//        if ( gasCylinderBindRelationDao.findLocateDevByCylinderId(gasCylinder.getId()) != null)
//        {
//            throw new ServerSideBusinessException("钢瓶已经绑定，请先解绑定！", HttpStatus.CONFLICT);
//        }
//
//        /*如果定位终端已经被绑定了，提示应先解绑定*/
//        if ( gasCylinderBindRelationDao.findGasCylinderByLocateDevId(locationDevice.getId()) != null)
//        {
//
//            throw new ServerSideBusinessException("定位终端已经绑定，请先解绑定！", HttpStatus.CONFLICT);
//        }
//
//        gasCylinderBindRelationDao.bindLocationDev(gasCylinder.getId(),locationDevice.getId());
//    }
//
//    @Override
//    @OperationLog(desc = "解除绑定关系")
//    public void unBindLocationDev(String gasCylinderNumber,String locationDeviceNumber)
//    {
//        /*参数校验*/
//        if (gasCylinderNumber == null || gasCylinderNumber.trim().length() == 0 )
//        {
//            throw new ServerSideBusinessException("参数错误，缺少钢瓶编号！", HttpStatus.NOT_ACCEPTABLE);
//        }
//
//        if (locationDeviceNumber == null || locationDeviceNumber.trim().length() == 0 )
//        {
//            throw new ServerSideBusinessException("参数错误，缺少定位终端编号！", HttpStatus.NOT_ACCEPTABLE);
//        }
//
//        /*钢瓶是否存在*/
//        Optional<GasCylinder> gasCylinderOptional = findByNumber(gasCylinderNumber);
//        if ( !gasCylinderOptional.isPresent())
//        {
//            throw new ServerSideBusinessException("钢瓶信息不存在！", HttpStatus.NOT_FOUND);
//        }
//        GasCylinder gasCylinder = gasCylinderOptional.get();
//
//
//        /*定位终端不存在*/
//        LocationDevice locationDevice = locationDeviceDao.findByNumber(locationDeviceNumber);
//        if (locationDevice == null)
//        {
//            throw new ServerSideBusinessException("定位终端信息不存在！", HttpStatus.NOT_FOUND);
//        }
//
//        /*如果钢瓶和定位终端没有被绑定，提示错误信息*/
//        if ( gasCylinderBindRelationDao.findBindRelation(gasCylinder.getId(),locationDevice.getId()) == null)
//        {
//            throw new ServerSideBusinessException("钢瓶和定位终端没有被绑定！", HttpStatus.NOT_FOUND);
//        }
//
//        gasCylinderBindRelationDao.unBindLocationDev(gasCylinder.getId(),locationDevice.getId());
//    }


}

package com.donno.nj.service.impl;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.dao.*;
import com.donno.nj.domain.*;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.service.GasCylinderService;
import com.donno.nj.util.AppUtil;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
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

    @Autowired
    private UserDao userDao;

    @Autowired
    private GasCynForceTakeOverWarnDao gasCynForceTakeOverWarnDao;

    @Autowired
    private GasCynUserRelDao gasCynUserRelDao;

    @Autowired
    GasCylinderSvcStatusOpHisDao gasCylinderSvcStatusOpHisDao;

    @Autowired
    GasCylinderInOutDao gasCylinderInOutDao;

    @Autowired
    SysUserDao sysUserDao;

    @Autowired
    GasCynFactoryDao gasCynFactoryDao;

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
        if (gasCylinder == null || gasCylinder.getNumber() == null || gasCylinder.getPublicNumber() == null||
                gasCylinder.getSpec() == null ||
                gasCylinder.getProductionDate() == null || gasCylinder.getVerifyDate() == null ||
                gasCylinder.getNextVerifyDate() == null || gasCylinder.getScrapDate() == null
                )
        {
            throw new ServerSideBusinessException("钢瓶信息不全，请补充钢瓶信息！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*厂家信息校验*/
        if (gasCylinder.getFactory()!= null )
        {
            GasCynFactory factory = gasCynFactoryDao.findByCode(gasCylinder.getFactory().getCode());
            if (factory != null)
            {
                gasCylinder.setFactory(factory);
            }
            else
            {
                throw new ServerSideBusinessException("钢瓶厂家信息错误！", HttpStatus.NOT_ACCEPTABLE);
            }
        }
        else
        {
            throw new ServerSideBusinessException("钢瓶厂家信息不能为空！", HttpStatus.NOT_ACCEPTABLE);
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

        gasCylinder.setLifeStatus(DeviceStatus.DevEnabled);
        gasCylinder.setServiceStatus(GasCynServiceStatus.StationStock);
        gasCylinderDao.insert(gasCylinder);

        /*当前用户获取*/
        Optional<User> user = AppUtil.getCurrentLoginUser();
        if (!user.isPresent())
        {
            throw new ServerSideBusinessException("会话失效,请重新登录！", HttpStatus.UNAUTHORIZED);
        }

        /*钢瓶责任人关联*/
        gasCynUserRelDao.bindUser(gasCylinder.getId(),user.get().getId());

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

        /*钢瓶厂家*/
        if (newGasCylinder.getFactory() != null && newGasCylinder.getFactory().getCode() != null && newGasCylinder.getFactory().getCode().trim().length() > 0)
        {
            GasCynFactory gasCynFactory =  gasCynFactoryDao.findByCode(newGasCylinder.getFactory().getCode());
            if (gasCynFactory != null)
            {
                newGasCylinder.setFactory(gasCynFactory);
            }
            else
            {
                throw new ServerSideBusinessException("钢瓶厂家信息不存在！", HttpStatus.NOT_ACCEPTABLE);
            }
        }
        else
        {
            newGasCylinder.setFactory(null);
        }

        /*更新数据*/
        gasCylinderDao.update(newGasCylinder);
    }

    @Override
    @OperationLog(desc = "修改钢瓶业务状态信息")
    public void updateSvcStatus(String number,Integer serviceStatus,String srcUserId,String targetUserId,Boolean enableForce ,String note)
    {
       /*参数校验*/
        if (number == null || number.trim().length() == 0 )
        {
            throw new ServerSideBusinessException("钢瓶编号不能为空！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*serviceStatus 取值检查*/
        if (serviceStatus < 0 || serviceStatus >= GasCynServiceStatus.values().length)
        {
            throw new ServerSideBusinessException("业务状态信息错误！", HttpStatus.NOT_FOUND);
        }

        /*钢瓶是否存在*/
        Optional<GasCylinder> gasCylinderOptional = findByNumber(number);
        if (!gasCylinderOptional.isPresent())
        {
            throw new ServerSideBusinessException("钢瓶信息不存在！", HttpStatus.NOT_FOUND);
        }

        /*如果钢瓶业务状态为待使用，则不校验原责任人*/
        User srcUser = new User();
        if (gasCylinderOptional.get().getServiceStatus().getIndex() != GasCynServiceStatus.UnUsed.getIndex())
        {
            /*用户是否存在*/
            srcUser = userDao.findByUserId(srcUserId);
            if (srcUser == null)
            {
                throw new ServerSideBusinessException("原用户不存在！", HttpStatus.NOT_FOUND);
            }

            /*强制交接时，不需要校验原责任人*/
            if (!enableForce)
            {
                 /*钢瓶当前责任人校验*/
                CheckGasCynUser(gasCylinderOptional.get(),serviceStatus,srcUser);
            }
        }

        User targetUser = userDao.findByUserId(targetUserId);
        if (targetUser == null)
        {
            throw new ServerSideBusinessException("目的用户不存在！", HttpStatus.NOT_FOUND);
        }

        /*钢瓶业务状态修改*/
        gasCylinderDao.updateSvcStatus(gasCylinderOptional.get().getId(),serviceStatus);

        /*关联责任人是否存在，若存在则更新*/
        GasCynUserRel gasCynUserRel = gasCynUserRelDao.findBindRel(gasCylinderOptional.get().getId());
        if (gasCynUserRel == null)
        {
            throw new ServerSideBusinessException("该钢瓶原责任人为空！", HttpStatus.NOT_ACCEPTABLE);
        }
        gasCynUserRelDao.updateBindedUser(gasCynUserRel.getId(),targetUser.getId());

        if (gasCylinderOptional.get().getServiceStatus().getIndex() != GasCynServiceStatus.UnUsed.getIndex())
        {
            /* 出入库*/
            GasCylinderInOut(gasCylinderOptional.get(),serviceStatus,srcUser,targetUser);

            /*空重瓶状态更改*/
            UpdGasCynLoadStatus(gasCylinderOptional.get(),serviceStatus);

             /*操作历史*/
            SvcStatusOpHis(srcUser,targetUser,serviceStatus,gasCylinderOptional.get(),enableForce,note);
        }
    }

    public void UpdGasCynLoadStatus(GasCylinder gasCylinder,Integer serviceStatus)
    {
        /*气站出库，更改为重瓶*/
        if ( ((gasCylinder.getServiceStatus().getIndex() == GasCynServiceStatus.StationStock.getIndex())
                        && (serviceStatus == GasCynServiceStatus.Transporting.getIndex())) //气站库存-->出库
                )
        {
            GasCylinder newGasCylinder = new GasCylinder();
            newGasCylinder.setId(gasCylinder.getId());
            newGasCylinder.setLoadStatus(LoadStatus.LSHeavy);
            gasCylinderDao.update(newGasCylinder);
        }

        /*空瓶回收，更改为轻瓶*/
//        if ( ((gasCylinder.getServiceStatus().getIndex() == GasCynServiceStatus.CustomerUsing.getIndex())
//                && (serviceStatus == GasCynServiceStatus.EmptyCynRetrieve.getIndex())) //用户使用-->回收
//                )
//        {
//            GasCylinder newGasCylinder = new GasCylinder();
//            newGasCylinder.setId(gasCylinder.getId());
//            newGasCylinder.setLoadStatus(LoadStatus.LSEmpty);
//            gasCylinderDao.update(newGasCylinder);
//        }

        /*空瓶回收，更改为轻瓶*/
        if ( serviceStatus == GasCynServiceStatus.EmptyCynRetrieve.getIndex()) //用户使用-->回收
        {
            GasCylinder newGasCylinder = new GasCylinder();
            newGasCylinder.setId(gasCylinder.getId());
            newGasCylinder.setLoadStatus(LoadStatus.LSEmpty);
            gasCylinderDao.update(newGasCylinder);
        }
    }

    public void CheckGasCynUser(GasCylinder gasCylinder,Integer serviceStatus,User srcUser)
    {
        if (gasCylinder.getUser() == null )
        {
            throw new ServerSideBusinessException("该钢瓶原责任人为空！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*出库时，只需校验是否同一个部门*/
        if ( ((gasCylinder.getServiceStatus().getIndex() == GasCynServiceStatus.StoreStock.getIndex())
                && (serviceStatus == GasCynServiceStatus.Dispatching.getIndex())) //门店库存-->派送
                ||
                ((gasCylinder.getServiceStatus().getIndex() == GasCynServiceStatus.StationStock.getIndex())
                        && (serviceStatus == GasCynServiceStatus.Transporting.getIndex())) //气站库存-->出库
                )
        {
            SysUser gasSysUser = sysUserDao.findBySysUserId(gasCylinder.getUser().getUserId());
            if (gasSysUser == null )
            {
                throw new ServerSideBusinessException("该钢瓶原责任人校验错误！未查找到钢瓶责任人", HttpStatus.NOT_ACCEPTABLE);
            }
            SysUser srcSysUser = sysUserDao.findBySysUserId(srcUser.getUserId());
            if (srcSysUser == null )
            {
                throw new ServerSideBusinessException("原责任人校验错误！未查找到钢瓶责任人", HttpStatus.NOT_ACCEPTABLE);
            }
            if ( gasSysUser.getDepartment().getId() != srcSysUser.getDepartment().getId() )
            {
                throw new ServerSideBusinessException("该钢瓶原责任人校验错误！当前用户没有操作权限！", HttpStatus.NOT_ACCEPTABLE);
            }
        }
        else
        {
            if ( !gasCylinder.getUser().getId().equals(srcUser.getId()))
            {
                throw new ServerSideBusinessException("该钢瓶原责任人校验错误！", HttpStatus.CONFLICT);
            }
        }
    }

    public void GasCylinderInOut(GasCylinder gasCylinder,Integer serviceStatus,User srcUser,User targetUser)
    {
        /*入库信息*/
        if ( ((gasCylinder.getServiceStatus().getIndex() == GasCynServiceStatus.Transporting.getIndex()) && (serviceStatus == GasCynServiceStatus.StoreStock.getIndex()))
                || ((gasCylinder.getServiceStatus().getIndex() == GasCynServiceStatus.Dispatching.getIndex()) && (serviceStatus == GasCynServiceStatus.StoreStock.getIndex())) )
        {
            GasCylinderInOut gasCylinderInOut = new GasCylinderInOut();
            gasCylinderInOut.setSrcUser(srcUser);
            gasCylinderInOut.setTargetUser(targetUser);
            gasCylinderInOut.setGasCylinder(gasCylinder);
            gasCylinderInOut.setStockType(StockType.STStockIn);
            gasCylinderInOut.setAmount(1);
            gasCylinderInOut.setOptime(new Date());
            gasCylinderInOutDao.insert(gasCylinderInOut);
        }

            /*出库信息*/
        if ( ((gasCylinder.getServiceStatus().getIndex() == GasCynServiceStatus.StoreStock.getIndex()) && (serviceStatus == GasCynServiceStatus.Dispatching.getIndex())))
        {
            GasCylinderInOut gasCylinderInOut = new GasCylinderInOut();
            gasCylinderInOut.setSrcUser(srcUser);
            gasCylinderInOut.setTargetUser(targetUser);
            gasCylinderInOut.setGasCylinder(gasCylinder);
            gasCylinderInOut.setStockType(StockType.STStockOut);
            gasCylinderInOut.setAmount(1);
            gasCylinderInOut.setOptime(new Date());
            gasCylinderInOutDao.insert(gasCylinderInOut);
        }
    }

    public void SvcStatusOpHis(User srcUser,User targetUser,Integer serviceStatus,GasCylinder gasCylinder,Boolean enableForce,String note)
    {
        GasCylinderSvcStatusOpHis gasCylinderSvcStatusOpHis = new GasCylinderSvcStatusOpHis();
        gasCylinderSvcStatusOpHis.setGasCylinder(gasCylinder);
        gasCylinderSvcStatusOpHis.setSrcServiceStatus(gasCylinder.getServiceStatus());
        gasCylinderSvcStatusOpHis.setServiceStatus(GasCynServiceStatus.values()[serviceStatus]);
        gasCylinderSvcStatusOpHis.setSrcUser(srcUser);
        gasCylinderSvcStatusOpHis.setTargetUser(targetUser);
        gasCylinderSvcStatusOpHis.setOptime(new Date() );

        //取经纬度信息
        SysUser sysSrcUser = sysUserDao.findBySysUserId(srcUser.getUserId());
        SysUser sysTargetUser = sysUserDao.findBySysUserId(targetUser.getUserId());
        UserPosition srcUserPosition = null;
        UserPosition targetUserPosition = null;
        if(sysSrcUser != null){
            srcUserPosition = sysSrcUser.getUserPosition();
        }
        if(sysTargetUser != null){
            targetUserPosition = sysTargetUser.getUserPosition();
        }
        //两个经纬度取一个
        if(srcUserPosition != null){
            gasCylinderSvcStatusOpHis.setLongitude(srcUserPosition.getLongitude());
            gasCylinderSvcStatusOpHis.setLatitude(srcUserPosition.getLatitude());
        } else if(targetUserPosition != null){
            gasCylinderSvcStatusOpHis.setLongitude(targetUserPosition.getLongitude());
            gasCylinderSvcStatusOpHis.setLatitude(targetUserPosition.getLatitude());
        }

        gasCylinderSvcStatusOpHis.setNote(note);
        gasCylinderSvcStatusOpHisDao.insert(gasCylinderSvcStatusOpHis);

        /*强制交接时，记录强制交接告警记录*/
        if (enableForce)
        {
            GasCynForceTakeOverWarn gasCynForceTakeOverWarn = new GasCynForceTakeOverWarn();
            gasCynForceTakeOverWarn.setGasCylinder(gasCylinder);
            gasCynForceTakeOverWarn.setSrcUser(srcUser);
            gasCynForceTakeOverWarn.setGasCylinderSvcStatusOpHis(gasCylinderSvcStatusOpHis);
            gasCynForceTakeOverWarn.setGasCynWarnStatus(GasCynWarnStatus.Created);
            gasCynForceTakeOverWarnDao.insert(gasCynForceTakeOverWarn);
        }
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

        /*钢瓶责任人关系表删除*/
        GasCynUserRel gasCynUserRel = gasCynUserRelDao.findBindRel(id);
        if (gasCynUserRel != null)
        {
            gasCynUserRelDao.delete(gasCynUserRel.getId());
        }

        /*钢瓶位置信息表信息删除*/

        /*钢瓶轨迹信息表信息删除*/

        /*钢瓶基础信息表数据删除*/
        gasCylinderDao.deleteByIdx(id);
    }
}

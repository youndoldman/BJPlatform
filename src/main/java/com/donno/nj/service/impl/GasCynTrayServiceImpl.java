package com.donno.nj.service.impl;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.dao.*;
import com.donno.nj.domain.*;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.service.GasCynTrayService;
import com.donno.nj.service.TicketService;
import com.donno.nj.util.Clock;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;



@Service
public class GasCynTrayServiceImpl implements GasCynTrayService
{
    @Autowired
    private SystemParamDao systemParamDao;


    @Autowired
    private GasCynTrayDao gasCynTrayDao;

    @Autowired
    private GasCynTrayBindRelationDao gasCynTrayBindRelationDao;

    @Autowired
    private UserDao userDao;


    @Override
    @OperationLog(desc = "查询托盘信息")
    public List<GasCynTray> retrieve(Map params)
    {
        return gasCynTrayDao.getList(params);
    }

    @Override
    @OperationLog(desc = "查询托盘数量")
    public Integer count(Map params)
    {
        return  gasCynTrayDao.count(params);
    }

    public void checkTrayNumber(String number)
    {
        if (number == null || number.trim().length() == 0)
        {
            throw new ServerSideBusinessException("缺少托盘编号！", HttpStatus.NOT_ACCEPTABLE);
        }

        GasCynTray gasCynTray = gasCynTrayDao.findByNumber(number);
        if (gasCynTray != null)
        {
            String message = String.format("托盘编号%s已经存在",number);
            throw new ServerSideBusinessException(message, HttpStatus.NOT_ACCEPTABLE);
        }
    }


    @Override
    @OperationLog(desc = "添加托盘信息")
    public void create(GasCynTray gasCynTray)
    {
        /*参数校验*/
        if (gasCynTray == null)
        {
            throw new ServerSideBusinessException("托盘信息不全，请补充信息！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*编号检查*/
        checkTrayNumber(gasCynTray.getNumber());

        gasCynTrayDao.insert(gasCynTray);
    }

    @Override
    @OperationLog(desc = "修改托盘信息")
    public void update(String number, GasCynTray newGasCynTray)
    {
        /*参数校验*/
        if (newGasCynTray == null )
        {
            throw new ServerSideBusinessException("托盘信息不能为空！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*托盘是否存在*/
        GasCynTray gasCynTray = gasCynTrayDao.findByNumber(number);
        if (gasCynTray == null)
        {
            throw new ServerSideBusinessException("托盘不存在！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*托盘新编号是否存在*/
        if (newGasCynTray.getNumber() != null && newGasCynTray.getNumber().trim().length() > 0)
        {
            if (gasCynTrayDao.findByNumber(newGasCynTray.getNumber()) == null)
            {
                throw new ServerSideBusinessException("托盘不存在！", HttpStatus.NOT_ACCEPTABLE);
            }
        }


        //增加了标定功能
        newGasCynTray.setValidWeight(gasCynTray.getWeight()+newGasCynTray.getCalibration());
        Integer warningWeight = systemParamDao.getTrayWarningWeight();
        if (newGasCynTray.getValidWeight() <= warningWeight)
        {
            newGasCynTray.setWarnningStatus(WarnningStatus.WSWarnning1);
        }
        else
        {
            newGasCynTray.setWarnningStatus(WarnningStatus.WSNormal);
        }

        /*更新数据*/
        newGasCynTray.setId(gasCynTray.getId());
        gasCynTrayDao.update(newGasCynTray);
    }


    @Override
    @OperationLog(desc = "建立绑定关系")
    public void bind(String trayNumber,String userId)
    {
        /*参数校验*/
        if (trayNumber == null || trayNumber.trim().length() == 0 )
        {
            throw new ServerSideBusinessException("托盘信息不全，缺少托盘编号！", HttpStatus.NOT_ACCEPTABLE);
        }

        if (userId == null || userId.trim().length() == 0 )
        {
            throw new ServerSideBusinessException("客户信息不全，缺少客户ID！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*托盘是否存在*/
        GasCynTray gasCynTray = gasCynTrayDao.findByNumber(trayNumber);
        if ( gasCynTray == null)
        {
            gasCynTray = new GasCynTray();
            gasCynTray.setNumber(trayNumber);
            gasCynTray.setDeviceStatus(DeviceStatus.DevEnabled);
            gasCynTrayDao.insert(gasCynTray);
        }

        /*如果托盘已经被绑定了，提示应先解绑定*/
        if ( gasCynTrayBindRelationDao.findRelationByNumber(trayNumber) != null)
        {
            throw new ServerSideBusinessException("该托盘已经绑定！", HttpStatus.CONFLICT);
        }

        /*客户是否存在*/
        User user = userDao.findByUserId(userId);
        if ( user == null)
        {
            throw new ServerSideBusinessException("客户不存在！", HttpStatus.NOT_FOUND);
        }

        gasCynTrayBindRelationDao.bind(gasCynTray.getId(),user.getId());
    }

    @Override
    @OperationLog(desc = "解除绑定关系")
    public void unBind(String trayNumber,String userId)
    {
        /*参数校验*/
        if (trayNumber == null || trayNumber.trim().length() == 0 )
        {
            throw new ServerSideBusinessException("托盘信息不全，缺少托盘编号！", HttpStatus.NOT_ACCEPTABLE);
        }

        if (userId == null || userId.trim().length() == 0 )
        {
            throw new ServerSideBusinessException("客户信息不全，缺少客户ID！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*托盘是否存在*/
        GasCynTray gasCynTray = gasCynTrayDao.findByNumber(trayNumber);
        if ( gasCynTray == null)
        {
            throw new ServerSideBusinessException("托盘信息不存在！", HttpStatus.NOT_FOUND);
        }

        /*客户是否存在*/
        User user = userDao.findByUserId(userId);
        if ( user == null)
        {
            throw new ServerSideBusinessException("该客户不存在！", HttpStatus.CONFLICT);
        }

        /*如果托盘没有被绑定，提示应先绑定*/
        if ( gasCynTrayBindRelationDao.findRelationByNumber(trayNumber) == null)
        {
            throw new ServerSideBusinessException("该托盘未绑定！", HttpStatus.NOT_FOUND);
        }

        gasCynTrayBindRelationDao.unBind(gasCynTray.getId(),user.getId());
    }


    @Override
    @OperationLog(desc = "删除托盘信息")
    public void deleteById(Integer id)
    {
        GasCynTray gasCynTray = gasCynTrayDao.findById(id);
        if (gasCynTray == null)
        {
            throw new ServerSideBusinessException("托盘信息不存在！", HttpStatus.NOT_FOUND);
        }

         /*已经存在关联客户的托盘不允许修改*/
        if(gasCynTrayBindRelationDao.findRelationByNumber(gasCynTray.getNumber()) != null)
        {
            throw new ServerSideBusinessException("托盘已经绑定，请先解绑！", HttpStatus.NOT_FOUND);
        }

        gasCynTrayDao.delete(id);
    }

    @Override
    @OperationLog(desc = "查找托盘信息")
    public Optional<GasCynTray> findById(Integer id)
    {
        return Optional.fromNullable(gasCynTrayDao.findById(id));
    }


    @Override
    @OperationLog(desc = "消除托盘的报警状态")
    public void removeWaningStatus(String number)
    {
        /*托盘是否存在*/
        GasCynTray gasCynTray = gasCynTrayDao.findByNumber(number);
        if (gasCynTray == null)
        {
            throw new ServerSideBusinessException("托盘不存在！", HttpStatus.NOT_ACCEPTABLE);
        }
        gasCynTray.setWarnningStatus(WarnningStatus.WSNormal);
        /*更新数据*/
        gasCynTrayDao.update(gasCynTray);
    }


}

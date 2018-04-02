package com.donno.nj.domain;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Date;

/*
* 钢瓶业务状态变更历史
* */
public class GasCylinderSvcStatusOpHis implements Serializable
{
    private Integer id;
    private GasCylinder gasCylinder;
    private GasCynServiceStatus serviceStatus;

    private User srcUser;
    private User targetUser;
    private Date optime;


    public GasCylinderSvcStatusOpHis()
    {
    }

    public Integer getId()
    {
        return id;
    }

    public GasCylinder gasCylinder()
    {
        return gasCylinder;
    }

    public GasCynServiceStatus getServiceStatus()
    {
        return  serviceStatus;
    }


    public User getSrcUser()
    {
        return srcUser;
    }

    public User getTargetUser()
    {
        return targetUser;
    }

    public  Date getOptime()
    {
        return  optime;
    }





    public void setId(Integer id)
    {
        this.id = id;
    }

    public void setGasCylinder(GasCylinder gasCylinder)
    {
        this.gasCylinder = gasCylinder;
    }

    public void setSrcUser(User srcUser)
    {
        this.srcUser = srcUser;
    }

    public void setTargetUser(User targetUser)
    {
        this.targetUser = targetUser;
    }


    public void  setServiceStatus(GasCynServiceStatus serviceStatus)
    {
        this.serviceStatus = serviceStatus;
    }

    public void  setOptime(Date optime)
    {
        this.optime = optime;
    }


    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .toString();
    }
}
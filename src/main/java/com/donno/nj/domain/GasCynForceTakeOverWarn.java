package com.donno.nj.domain;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Date;

/*钢瓶强制交接告警记录*/
public class GasCynForceTakeOverWarn implements Serializable
{
    private Integer id;
    private GasCylinder gasCylinder;//钢瓶
    private User srcUser; //原责任人
    private GasCylinderSvcStatusOpHis gasCylinderSvcStatusOpHis;//交接记录

    private GasCynWarnStatus  gasCynWarnStatus;

    protected String  note;
    protected Date createTime;
    protected Date updateTime;

    public GasCynForceTakeOverWarn()
    {
    }

    public Integer getId()
    {
        return id;
    }

    public GasCylinder getGasCylinder() {
        return gasCylinder;
    }

    public GasCylinderSvcStatusOpHis getGasCylinderSvcStatusOpHis() {
        return gasCylinderSvcStatusOpHis;
    }

    public User getSrcUser() {
        return srcUser;
    }

    public GasCynWarnStatus getGasCynWarnStatus() {
        return gasCynWarnStatus;
    }

    public String getNote()
    {
        return note;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public Date getUpdateTime()
    {
        return updateTime;
    }


    public void setId(Integer id)
    {
        this.id = id;
    }


    public void setGasCylinder(GasCylinder gasCylinder)
    {
        this.gasCylinder = gasCylinder;
    }

    public void setGasCylinderSvcStatusOpHis(GasCylinderSvcStatusOpHis gasCylinderSvcStatusOpHis)
    {
        this.gasCylinderSvcStatusOpHis = gasCylinderSvcStatusOpHis;
    }

    public void setGasCynWarnStatus(GasCynWarnStatus gasCynWarnStatus) {
        this.gasCynWarnStatus = gasCynWarnStatus;
    }

    public void setSrcUser(User srcUser) {
        this.srcUser = srcUser;
    }

    public void setNote(String note)
    {
        this.note = note;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .toString();
    }
}
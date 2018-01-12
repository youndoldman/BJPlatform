package com.donno.nj.domain;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Date;

public class GasCylinderBindRelation implements Serializable
{
    private Integer id;

    private GasCylinder gasCylinder;
    private LocationDevice locationDevice;

    private String note;
    private Date createTime;
    private Date updateTime;

    public GasCylinderBindRelation()
    {
    }

    public Integer getId()
    {
        return id;
    }

    public GasCylinder getGasCylinder()
    {
        return gasCylinder;
    }

    public LocationDevice getLocationDevice()
    {
        return locationDevice;
    }


    public  Date getCreateTime()
    {
        return  createTime;
    }

    public  Date getUpdateTime()
    {
        return  updateTime;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public void setGasCylinder(GasCylinder gasCylinder)
    {
        this.gasCylinder = gasCylinder;
    }

    public void setLocationDevice(LocationDevice locationDevice)
    {
        this.locationDevice = locationDevice;
    }

    public void setNote(String note)
    {
        this.note = note;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public  void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }


    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("note", note)
                .add("createTime", createTime)
                .add("updateTime", updateTime)
                .toString();
    }
}
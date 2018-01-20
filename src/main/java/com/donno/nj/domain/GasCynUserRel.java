package com.donno.nj.domain;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Date;


public class GasCynUserRel implements Serializable
{
    private Integer id;

    private GasCylinder gasCylinder;
    private User user;

    private Date createTime;
    private Date updateTime;

    public GasCynUserRel()
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

    public User getUser()
    {
        return user;
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

    public void setUser(User user)
    {
        this.user = user;
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
                .add("createTime", createTime)
                .add("updateTime", updateTime)
                .toString();
    }
}
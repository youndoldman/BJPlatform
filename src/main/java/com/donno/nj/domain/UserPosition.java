package com.donno.nj.domain;


import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Date;

public class UserPosition implements Serializable
{
    private Integer id;
    private Integer userIdx;
    private double longitude;
    private double latitude;
    private Date createTime;
    private Date updateTime;

    public UserPosition()
    {
    }

    public Integer getId()
    {
        return id;
    }

    public Integer getUserIdx()
    {
        return userIdx;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public double getLatitude()
    {
        return  latitude;
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

    public void setUserIdx(Integer userIdx)
    {
        this.userIdx = userIdx;
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
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
                .add("id", id)
                .add("createTime", createTime)
                .add("updateTime", updateTime)
                .toString();
    }
}

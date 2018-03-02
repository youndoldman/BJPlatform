package com.donno.nj.domain;


import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Date;

public class GasCylinderPosition implements Serializable
{
    private String code;
    private String location;
    private String createTime;

    public GasCylinderPosition()
    {
    }

    public GasCylinderPosition(String code, String location, String createTime)
    {
        this.code = code;
        this.location = location;
        this.createTime = createTime;
    }

    public String getCode()
    {
        return code;
    }


    public String getLocation()
    {
        return location;
    }

    public String getCreateTime()
    {
        return createTime;
    }


    public void setCode(String code)
    {
        this.code = code;
    }


    public void setLocation(String location)
    {
        this.location = location;
    }


    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }


    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("code", code)
                .add("location", location)
                .add("createTime", createTime)
                .toString();
    }
}

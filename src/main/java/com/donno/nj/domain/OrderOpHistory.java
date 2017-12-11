package com.donno.nj.domain;

import java.util.Date;

public class OrderOpHistory
{
    private Integer id;
    private Integer orderIdx;
    private Integer operIdx;
    private String opLog;
    private Date updateTime;

    public Integer getId()
    {
        return id;
    }

    public Integer getOrderIdx()
    {
        return  orderIdx;
    }

    public Integer getOperIdx()
    {
        return operIdx;
    }

    public String getOpLog()
    {
        return opLog;
    }

    public Date getUpdateTime()
    {
        return updateTime;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public void setOrderIdx(Integer orderIdx)
    {
        this.orderIdx = orderIdx;
    }

    public void setOperIdx(Integer operIdx)
    {
        this.orderIdx = operIdx;
    }

    public void setOpLog(String opLog)
    {
        this.opLog = opLog;
    }

    public void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }

}

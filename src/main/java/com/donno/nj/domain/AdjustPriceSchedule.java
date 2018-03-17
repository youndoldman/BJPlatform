package com.donno.nj.domain;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class AdjustPriceSchedule implements Serializable
{
    private Integer id;
    private String  name;
    private Date effectTime;//生效时间
    private AdjustPriceScheduleStatus status;//调价计划状态
    List<AdjustPriceDetail> adjustPriceDetailList;
    protected String note;
    protected Date createTime;
    protected Date updateTime;

    public AdjustPriceSchedule()
    {
    }

    public Integer getId()
    {
        return id;
    }

    public  String getName()
    {
        return  name;
    }

    public Date getEffectTime()
    {
        return effectTime;
    }

    public AdjustPriceScheduleStatus getStatus()
    {
        return status;
    }


    public List<AdjustPriceDetail> getAdjustPriceDetailList()
    {
        return adjustPriceDetailList;
    }

    public String getNote()
    {
        return note;
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

    public void setName(String name)
    {
        this.name = name;
    }

    public void setEffectTime(Date effectTime)
    {
        this.effectTime = effectTime;
    }

    public  void setStatus(AdjustPriceScheduleStatus status)
    {
        this.status = status;
    }

    public void setAdjustPriceDetailList(List<AdjustPriceDetail> adjustPriceDetailList)
    {
        this.adjustPriceDetailList = adjustPriceDetailList;
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
                .add("name", name)
                .add("note", note)
                .add("createTime", createTime)
                .add("updateTime", updateTime)
                .toString();
    }
}
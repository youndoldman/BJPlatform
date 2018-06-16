package com.donno.nj.domain;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Date;

public class GasCynTrayBindRelation implements Serializable
{
    private Integer id;

    private GasCynTray gasCynTray;
    private Customer customer;

    private String note;
    private Date createTime;
    private Date updateTime;

    public GasCynTrayBindRelation()
    {
    }

    public Integer getId()
    {
        return id;
    }

    public GasCynTray getGasCynTray() {
        return gasCynTray;
    }

    public Customer getCustomer() {
        return customer;
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

    public void setGasCynTray(GasCynTray gasCynTray) {
        this.gasCynTray = gasCynTray;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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
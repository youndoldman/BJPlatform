package com.donno.nj.domain;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Date;

/*
* 钢瓶在门店的出入库记录
* */


public class GasCylinderInOut implements Serializable
{
    private Integer id;
    private GasCylinder gasCylinder;
    private StockType stockType;  //入库/出库类型  0入库 1 出库
    private Integer amount;

    private User srcUser;
    private User targetUser;
    private Date opTime;

    public GasCylinderInOut()
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

    public StockType getStockType()
    {
        return  stockType;
    }

    public Integer getAmount()
    {
        return  amount;
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
        return  opTime;
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


    public void  setStockType(StockType stockType)
    {
        this.stockType = stockType;
    }

    public void  setAmount(Integer amount)
    {
        this.amount = amount;
    }

    public void  setOptime(Date opTime)
    {
        this.opTime = opTime;
    }


    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .toString();
    }
}
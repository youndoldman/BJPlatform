package com.donno.nj.domain;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
 

/*
* 钢瓶在门店的库存统计
* */


public class GasCylinderStockStatics implements Serializable
{
    private Department department;
    private GasCylinderSpec gasCylinderSpec;
    private Integer amount;

    public GasCylinderStockStatics()
    {
    }

    public Department getDepartment()
    {
        return department;
    }

    public GasCylinderSpec gasCylinderSpec()
    {
        return gasCylinderSpec;
    }


    public Integer getAmount()
    {
        return  amount;
    }
    public void setDepartment(Department department)
    {
        this.department = department;
    }
    public void setGasCylinderSpec(GasCylinderSpec gasCylinderSpec)
    {
        this.gasCylinderSpec = gasCylinderSpec;
    }
    public void  setAmount(Integer amount)
    {
        this.amount = amount;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .toString();
    }
}
package com.donno.nj.domain;

import com.donno.nj.domain.StockType;
import com.google.common.base.MoreObjects;

import java.io.Serializable;
 

/*
* 钢瓶在门店的出入库统计
* */


public class GasCylinderStockInOutStatics implements Serializable
{
    private Department department;
    private GasCylinderSpec gasCylinderSpec;
    private StockType stockType;  //入库/出库类型  0入库 1 出库
    private Integer amount;

    public GasCylinderStockInOutStatics()
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

    public StockType getStockType()
    {
        return  stockType;
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
    public void  setStockType(StockType stockType)
    {
        this.stockType = stockType;
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
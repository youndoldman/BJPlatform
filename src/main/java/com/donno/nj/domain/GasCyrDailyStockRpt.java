package com.donno.nj.domain;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Date;

/*
* 钢瓶库存报表（门店）
* */


public class GasCyrDailyStockRpt implements Serializable
{
    private  Integer id;
    private Department department;
    private GasCylinderSpec gasCylinderSpec;
    private Integer stockInAmount;  //入库数量
    private Integer stockOutAmount;  //出库数量
    private Integer stockAmount;     //库存数量
    private Integer monthStockInAmount;     //当月累计调入
    private Date date;

    public GasCyrDailyStockRpt()
    {
    }

    public Integer getId(){return  id;}

    public Department getDepartment()
    {
        return department;
    }

    public GasCylinderSpec gasCylinderSpec()
    {
        return gasCylinderSpec;
    }

    public Integer getStockInAmount()
    {
        return  stockInAmount;
    }

    public Integer getStockOutAmount()
    {
        return  stockOutAmount;
    }

    public Integer getStockAmount()
    {
        return  stockAmount;
    }

    public Integer getMonthStockInAmount()
    {
        return  monthStockInAmount;
    }

    public Date getDate()
    {
        return date;
    }

    public  void  setId(Integer id){ this.id = id;}
    public void setDepartment(Department department)
    {
        this.department = department;
    }
    public void setGasCylinderSpec(GasCylinderSpec gasCylinderSpec)
    {
        this.gasCylinderSpec = gasCylinderSpec;
    }
    public void setStockInAmount(Integer stockInAmount)
    {
        this.stockInAmount = stockInAmount;
    }
    public void  setStockOutAmount(Integer stockOutAmount)
    {
        this.stockOutAmount = stockOutAmount;
    }

    public void  setStockAmount(Integer stockAmount)
    {
        this.stockAmount = stockAmount;
    }
    public void  setMonthStockInAmount(Integer monthStockInAmount)
    {
        this.monthStockInAmount = monthStockInAmount;
    }
    public void setDate(Date date)
    {
        this.date = date;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .toString();
    }
}
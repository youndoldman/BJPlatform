package com.donno.nj.domain;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Date;

/*
* 钢瓶销售往来日报表（门店）
* */


public class GasCyrSaleContactsRpt implements Serializable
{
    private Integer id;
    private Department department;
    private Goods goods;
    private Double creditPayment;  //赊销回款
    private Double creditSurplus;  //赊销结余
    private Double monthlyPayment;     //月结回款
    private Double monthlySurplus;     //月结结余
    private Date date;

    public GasCyrSaleContactsRpt()
    {
    }

    public Integer getId(){return  id;}

    public Department getDepartment()
    {
        return department;
    }

    public Goods getGoods()
    {
        return goods;
    }

    public Double getCreditPayment()
    {
        return  creditPayment;
    }

    public Double getCreditSurplus()
    {
        return  creditSurplus;
    }

    public Double getMonthlyPayment()
    {
        return  monthlyPayment;
    }

    public Double getMonthlySurplus()
    {
        return  monthlySurplus;
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
    public void setGoods(Goods goods)
    {
        this.goods = goods;
    }
    public void setCreditPayment(Double creditPayment)
    {
        this.creditPayment = creditPayment;
    }
    public void  setCreditSurplus(Double creditSurplus)
    {
        this.creditSurplus = creditSurplus;
    }

    public void  setMonthlyPayment(Double monthlyPayment)
    {
        this.monthlyPayment = monthlyPayment;
    }
    public void  setMonthlySurplus(Double monthlySurplus)
    {
        this.monthlySurplus = monthlySurplus;
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
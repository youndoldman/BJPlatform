package com.donno.nj.domain;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Date;

/*
* 销售报表（门店）
* */


public class SalesRpt implements Serializable
{
    private Department department;
    private Goods goods;
    private Integer count;  //数量
    private Double  sum;    //金额

    public SalesRpt()
    {
    }


    public Department getDepartment()
    {
        return department;
    }

    public Goods getGoods()
    {
        return goods;
    }

    public Integer getCount()
    {
        return  count;
    }

    public Double getSum()
    {
        return  sum;
    }

    public void setDepartment(Department department)
    {
        this.department = department;
    }
    public void setGoods(Goods goods)
    {
        this.goods = goods;
    }
    public void setCount(Integer count)
    {
        this.count = count;
    }
    public void  setSum(Double sum)
    {
        this.sum = sum;
    }


    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .toString();
    }
}
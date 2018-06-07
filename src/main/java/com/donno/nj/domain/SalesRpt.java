package com.donno.nj.domain;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Date;

/*
* 销售报表（门店）
* */


public class SalesRpt implements Serializable
{
    private String departmentCode;
    private String departmentName;
    private String specCode;
    private String specName;
    private Integer count;  //数量
    private Float  sum;    //金额

    public SalesRpt()
    {
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public String getSpecCode() {
        return specCode;
    }

    public String getSpecName() {
        return specName;
    }

    public Integer getCount()
    {
        return  count;
    }

    public Float getSum()
    {
        return  sum;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public void setSpecCode(String specCode) {
        this.specCode = specCode;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public void setCount(Integer count)
    {
        this.count = count;
    }
    public void  setSum(Float sum)
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
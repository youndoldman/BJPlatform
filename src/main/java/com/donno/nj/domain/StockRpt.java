package com.donno.nj.domain;

import com.google.common.base.MoreObjects;

import java.io.Serializable;

/*
* 库存报表（门店）
* */


public class StockRpt implements Serializable
{
    private String departmentCode;
    private String departmentName;
    private String specCode;
    private String specName;
    private Integer amount;  //数量

    public StockRpt()
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

    public Integer getAmount()
    {
        return  amount;
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

    public void setAmount(Integer amount)
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
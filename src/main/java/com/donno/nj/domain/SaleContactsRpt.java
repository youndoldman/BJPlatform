package com.donno.nj.domain;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Date;

/*
* 销售往来日报表（门店）
* */


public class SaleContactsRpt implements Serializable
{
    private String departmentCode;
    private String departmentName;
    private Float sum;  //金额

    public SaleContactsRpt()
    {
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public Float getSum() {
        return sum;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public void setSum(Float sum) {
        this.sum = sum;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .toString();
    }
}
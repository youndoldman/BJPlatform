package com.donno.nj.domain;

import com.google.common.base.MoreObjects;

import java.io.Serializable;

/*
* 钢瓶动态（领用、送检、退维修、退报废瓶、退押金瓶、押瓶）信息统计（门店）
* */


public class GasCyrDynRpt implements Serializable
{
    private String departmentCode;
    private String departmentName;
    private String specCode;
    private String specName;
    private Integer amount;  //数量

    public GasCyrDynRpt()
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
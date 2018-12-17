package com.donno.nj.domain;

import com.google.common.base.MoreObjects;

import java.io.Serializable;

/*
* 按斤销售报表（门店）
* */


public class SalesRptByWeight implements Serializable
{
    private String departmentCode;
    private String departmentName;
    private String customerTypeCode;
    private String customerTypeName;
    private Float saleWeight;  //销售公斤数
    private Float  saleSum;    //销售金额
    private Float refoundWeight;  //退残液公斤数
    private Float  refoundSum;    //退残液金额

    public SalesRptByWeight()
    {
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public String getCustomerTypeCode() {
        return customerTypeCode;
    }

    public String getCustomerTypeName() {
        return customerTypeName;
    }

    public Float getSaleWeight() {
        return saleWeight;
    }

    public Float getSaleSum() {
        return saleSum;
    }

    public Float getRefoundWeight() {
        return refoundWeight;
    }

    public Float getRefoundSum() {
        return refoundSum;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public void setCustomerTypeCode(String customerTypeCode) {
        this.customerTypeCode = customerTypeCode;
    }

    public void setCustomerTypeName(String customerTypeName) {
        this.customerTypeName = customerTypeName;
    }

    public void setSaleWeight(Float saleWeight) {
        this.saleWeight = saleWeight;
    }

    public void setSaleSum(Float saleSum) {
        this.saleSum = saleSum;
    }

    public void setRefoundWeight(Float refoundWeight) {
        this.refoundWeight = refoundWeight;
    }

    public void setRefoundSum(Float refoundSum) {
        this.refoundSum = refoundSum;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .toString();
    }
}
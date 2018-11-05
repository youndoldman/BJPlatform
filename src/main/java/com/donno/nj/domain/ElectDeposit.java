package com.donno.nj.domain;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ElectDeposit implements Serializable
{
    private Integer id;
    private String depositSn;//押金单编号
    private String customerId; //客户userID
    private String customerName;//客户userName
    private String operId;//操作员userId（派送工）
    private String operName;//操作员userName（派送工）
    private Department operDep;//操作员所属部门（当前部门）

    private  ElectDepositStatus electDepositStatus;//押金单状态

    List<ElectDepositDetail>  electDepositDetails;//押金单明细
    private Float amountReceivable;//应收金额
    private Float actualAmount;//实收金额

    private String note;
    private Date createTime;
    private Date updateTime;

    public ElectDeposit()
    {
    }

    /*
    * 属性读取
    * */

    public Integer getId()
    {
        return id;
    }

    public String getDepositSn() {
        return depositSn;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getOperId() {
        return operId;
    }

    public String getOperName() {
        return operName;
    }

    public Department getOperDep() {
        return operDep;
    }

    public ElectDepositStatus getElectDepositStatus() {
        return electDepositStatus;
    }

    public List<ElectDepositDetail> getElectDepositDetails() {
        return electDepositDetails;
    }

    public Float getAmountReceivable() {
        return amountReceivable;
    }

    public Float getActualAmount() {
        return actualAmount;
    }

    public String getNote() {
        return note;
    }

    public  Date getCreateTime()
    {
        return  createTime;
    }

    public  Date getUpdateTime()
    {
        return  updateTime;
    }

    /*
    * 属性设置
    * */


    public void setId(Integer id)
    {
        this.id = id;
    }

    public void setDepositSn(String depositSn) {
        this.depositSn = depositSn;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setOperId(String operId) {
        this.operId = operId;
    }

    public void setOperName(String operName) {
        this.operName = operName;
    }

    public void setOperDep(Department operDep) {
        this.operDep = operDep;
    }

    public void setElectDepositStatus(ElectDepositStatus electDepositStatus) {
        this.electDepositStatus = electDepositStatus;
    }

    public void setElectDepositDetails(List<ElectDepositDetail> electDepositDetails) {
        this.electDepositDetails = electDepositDetails;
    }

    public void setAmountReceivable(Float amountReceivable) {
        this.amountReceivable = amountReceivable;
    }

    public void setActualAmount(Float actualAmount) {
        this.actualAmount = actualAmount;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public  void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }



    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .toString();
    }
}
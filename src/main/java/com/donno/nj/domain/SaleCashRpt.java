package com.donno.nj.domain;

import com.google.common.base.MoreObjects;

import java.io.Serializable;

/*
* 销售往来日报表（门店）
* */


public class SaleCashRpt implements Serializable
{
    private String departmentCode;
    private String departmentName;

    private Double saleCash;  //现金销款(现金销售额
    private Double ticketSaleCash;  //气票销售
    private Double depositCash;  //存银行款

    private Double creditWriteOff;  //赊销回款
    private Double montlyCreditWriteOff;  //月结回款

    private Double accCredit ;  //往日赊销（截止当前待收回款）
    private Double accMonthlyCredit ;  //往日月结（截止当前待收回款）

    private Double surplusCash; //今日结存现金（现金销售-银行存款+回款）

    public SaleCashRpt()
    {
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public Double getSaleCash() {
        return saleCash;
    }

    public Double getTicketSaleCash() {
        return ticketSaleCash;
    }

    public Double getDepositCash() {
        return depositCash;
    }

    public Double getCreditWriteOff() {
        return creditWriteOff;
    }

    public Double getMontlyCreditWriteOff() {
        return montlyCreditWriteOff;
    }

    public Double getAccCredit() {
        return accCredit;
    }

    public Double getAccMonthlyCredit() {
        return accMonthlyCredit;
    }

    public Double getSurplusCash() {
        return surplusCash;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public void setSaleCash(Double saleCash) {
        this.saleCash = saleCash;
    }

    public void setTicketSaleCash(Double ticketSaleCash) {
        this.ticketSaleCash = ticketSaleCash;
    }

    public void setDepositCash(Double depositCash) {
        this.depositCash = depositCash;
    }

    public void setCreditWriteOff(Double creditWriteOff) {
        this.creditWriteOff = creditWriteOff;
    }

    public void setMontlyCreditWriteOff(Double montlyCreditWriteOff) {
        this.montlyCreditWriteOff = montlyCreditWriteOff;
    }

    public void setAccCredit(Double accCredit) {
        this.accCredit = accCredit;
    }

    public void setAccMonthlyCredit(Double accMonthlyCredit) {
        this.accMonthlyCredit = accMonthlyCredit;
    }

    public void setSurplusCash(Double surplusCash) {
        this.surplusCash = surplusCash;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .toString();
    }
}
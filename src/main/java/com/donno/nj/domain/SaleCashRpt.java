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

    private Float saleCash;  //现金销款(现金销售额
    private Float ticketSaleCash;  //气票销售
    private Float depositCash;  //存银行款

    private Float creditWriteOff;  //赊销回款
    private Float montlyCreditWriteOff;  //月结回款

    private Float accCredit ;  //往日赊销（截止当前待收回款）
    private Float accMonthlyCredit ;  //往日月结（截止当前待收回款）

    private Float surplusCash; //今日结存现金（现金销售-银行存款+回款+气票款）

    public SaleCashRpt()
    {
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public Float getSaleCash() {
        return saleCash;
    }

    public Float getTicketSaleCash() {
        return ticketSaleCash;
    }

    public Float getDepositCash() {
        return depositCash;
    }

    public Float getCreditWriteOff() {
        return creditWriteOff;
    }

    public Float getMontlyCreditWriteOff() {
        return montlyCreditWriteOff;
    }

    public Float getAccCredit() {
        return accCredit;
    }

    public Float getAccMonthlyCredit() {
        return accMonthlyCredit;
    }

    public Float getSurplusCash() {
        return surplusCash;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public void setSaleCash(Float saleCash) {
        this.saleCash = saleCash;
    }

    public void setTicketSaleCash(Float ticketSaleCash) {
        this.ticketSaleCash = ticketSaleCash;
    }

    public void setDepositCash(Float depositCash) {
        this.depositCash = depositCash;
    }

    public void setCreditWriteOff(Float creditWriteOff) {
        this.creditWriteOff = creditWriteOff;
    }

    public void setMontlyCreditWriteOff(Float montlyCreditWriteOff) {
        this.montlyCreditWriteOff = montlyCreditWriteOff;
    }

    public void setAccCredit(Float accCredit) {
        this.accCredit = accCredit;
    }

    public void setAccMonthlyCredit(Float accMonthlyCredit) {
        this.accMonthlyCredit = accMonthlyCredit;
    }

    public void setSurplusCash(Float surplusCash) {
        this.surplusCash = surplusCash;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .toString();
    }
}
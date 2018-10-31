package com.donno.nj.domain;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Date;

public class ElectDeposit implements Serializable
{
    private Integer id;
    private String userId;
    private String userName;

    private  GasCylinderSpec  gasCylinderSpec;  //钢瓶规格
    private  ElectDepositType electDepositType; //押金单类型

    private Integer quantity;//数量
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

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public GasCylinderSpec getGasCylinderSpec() {
        return gasCylinderSpec;
    }

    public ElectDepositType getElectDepositType() {
        return electDepositType;
    }

    public Integer getQuantity() {
        return quantity;
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

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setGasCylinderSpec(GasCylinderSpec gasCylinderSpec) {
        this.gasCylinderSpec = gasCylinderSpec;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setAmountReceivable(Float amountReceivable) {
        this.amountReceivable = amountReceivable;
    }

    public void setActualAmount(Float actualAmount) {
        this.actualAmount = actualAmount;
    }

    public void setElectDepositType(ElectDepositType electDepositType) {
        this.electDepositType = electDepositType;
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
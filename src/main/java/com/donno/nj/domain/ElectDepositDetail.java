package com.donno.nj.domain;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Date;

public class ElectDepositDetail implements Serializable
{
    private Integer id;
    private Integer electDepositIdx;
    private  ElectDepositType electDepositType; //押金单类型
    private  GasCylinderSpec  gasCylinderSpec;  //钢瓶规格
    private Integer quantity;//数量


    private String note;
    private Date createTime;
    private Date updateTime;

    public ElectDepositDetail()
    {
    }

    /*
    * 属性读取
    * */

    public Integer getId()
    {
        return id;
    }

    public Integer getElectDepositIdx() {
        return electDepositIdx;
    }

    public ElectDepositType getElectDepositType() {
        return electDepositType;
    }

    public GasCylinderSpec getGasCylinderSpec() {
        return gasCylinderSpec;
    }



    public Integer getQuantity() {
        return quantity;
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

    public void setElectDepositIdx(Integer electDepositIdx) {
        this.electDepositIdx = electDepositIdx;
    }

    public void setGasCylinderSpec(GasCylinderSpec gasCylinderSpec) {
        this.gasCylinderSpec = gasCylinderSpec;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
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
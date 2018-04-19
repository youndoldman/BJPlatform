package com.donno.nj.domain;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Date;

/*钢瓶*/
public class GasCyrDynDetail implements Serializable
{
    private Integer id;
    private String operUserId;  //操作员userid
    private String gasCyrSpecCode; //钢瓶规格编码
    private String gasCyrSpecName;
    private GasCyrDynOperType gasCyrDynOperType;//动态类型

    private Integer amount;


    protected String  note;
    protected Date createTime;
    protected Date updateTime;


    public GasCyrDynDetail()
    {
    }

    public Integer getId()
    {
        return id;
    }

    public String getOperUserId() {
        return operUserId;
    }

    public String getGasCyrSpecCode() {
        return gasCyrSpecCode;
    }

    public String getGasCyrSpecName() {
        return gasCyrSpecName;
    }

    public GasCyrDynOperType getGasCyrDynOperType() {
        return gasCyrDynOperType;
    }



    public Integer getAmount() {
        return amount;
    }

    public String getNote()
    {
        return note;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public Date getUpdateTime()
    {
        return updateTime;
    }


    public void setId(Integer id) {
        this.id = id;
    }

    public void setOperUserId(String operUserId) {
        this.operUserId = operUserId;
    }

    public void setGasCyrSpecCode(String gasCyrSpecCode) {
        this.gasCyrSpecCode = gasCyrSpecCode;
    }

    public void setGasCyrSpecName(String gasCyrSpecName) {
        this.gasCyrSpecName = gasCyrSpecName;
    }

    public void setGasCyrDynOperType(GasCyrDynOperType gasCyrDynOperType) {
        this.gasCyrDynOperType = gasCyrDynOperType;
    }



    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public void setNote(String note)
    {
        this.note = note;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public void setUpdateTime(Date updateTime)
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
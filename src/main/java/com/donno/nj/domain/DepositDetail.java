package com.donno.nj.domain;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Date;

/*回款*/
public class DepositDetail implements Serializable
{
    private  Integer id;
    private  String operId;//操作员ID

    private  Float amount ;//金额

    protected Date operTime;

    protected String  note;
    protected Date createTime;
    protected Date updateTime;
    public DepositDetail()
    {
    }

    public Integer getId()
    {
        return id;
    }

    public String getOperId() {
        return operId;
    }

    public Float getAmount() {
        return amount;
    }

    public Date getOperTime() {
        return operTime;
    }

    public String getNote()
    {
        return note;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public void setOperId(String operId) {
        this.operId = operId;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public void setOperTime(Date operTime) {
        this.operTime = operTime;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .toString();
    }
}
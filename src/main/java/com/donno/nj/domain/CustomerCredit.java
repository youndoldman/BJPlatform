package com.donno.nj.domain;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Date;

/*欠款*/
public class CustomerCredit implements Serializable
{
    private Integer id;
    private  String userId;//客户ID

    private CreditType creditType;

    private  Double amount ;//金额
    protected String  note;
    protected Date createTime;
    protected Date updateTime;

    public CustomerCredit()
    {
    }

    public Integer getId()
    {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public CreditType getCreditType() {
        return creditType;
    }

    public Double getAmount() {
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

    public void setId(Integer id)
    {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setCreditType(CreditType creditType) {
        this.creditType = creditType;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setNote(String note) {
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
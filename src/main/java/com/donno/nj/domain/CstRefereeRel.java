package com.donno.nj.domain;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
/*
* 客户与推荐人关联
* */
public class CstRefereeRel implements Serializable
{
    private Integer id;

    private User customer;//客户
    private User referee;//关联推荐人
    protected String  note;
    private Date createTime;
    private Date updateTime;

    public Integer getId()
    {
        return id;
    }

    public User getCustomer() {
        return customer;
    }

    public User getReferee() {
        return referee;
    }

    public  Date getCreateTime()
    {
        return  createTime;
    }

    public  Date getUpdateTime()
    {
        return  updateTime;
    }

    public String getNote()
    {
        return note;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public void setReferee(User referee) {
        this.referee = referee;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public  void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }

    public void setNote(String note)
    {
        this.note = note;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("createTime", createTime)
                .add("updateTime", updateTime)
                .toString();
    }
}
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

    private String customerId;//客户userID
    private String refereeId;//关联推荐人

    private Date createTime;
    private Date updateTime;

    public Integer getId()
    {
        return id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getRefereeId() {
        return refereeId;
    }

    public  Date getCreateTime()
    {
        return  createTime;
    }

    public  Date getUpdateTime()
    {
        return  updateTime;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setRefereeId(String refereeId) {
        this.refereeId = refereeId;
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
                .add("id", id)
                .add("createTime", createTime)
                .add("updateTime", updateTime)
                .toString();
    }
}
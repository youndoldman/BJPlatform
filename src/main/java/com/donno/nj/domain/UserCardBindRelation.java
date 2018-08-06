package com.donno.nj.domain;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Date;

public class UserCardBindRelation implements Serializable
{
    private Integer id;

    private UserCard userCard;
    private Customer customer;

    private String note;
    private Date createTime;
    private Date updateTime;

    public UserCardBindRelation()
    {
    }

    public Integer getId()
    {
        return id;
    }

    public UserCard getUserCard() {
        return userCard;
    }

    public Customer getCustomer() {
        return customer;
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



    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setUserCard(UserCard userCard) {
        this.userCard = userCard;
    }

    public void setNote(String note)
    {
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
                .add("id", id)
                .add("note", note)
                .add("createTime", createTime)
                .add("updateTime", updateTime)
                .toString();
    }
}
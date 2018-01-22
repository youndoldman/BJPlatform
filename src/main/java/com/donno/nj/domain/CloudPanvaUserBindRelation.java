package com.donno.nj.domain;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Date;

public class CloudPanvaUserBindRelation implements Serializable
{
    private Integer id;

    private CloudUser cloudUser;
    private User panvaUser;

    private String note;
    private Date createTime;
    private Date updateTime;

    public CloudPanvaUserBindRelation()
    {
    }

    public Integer getId()
    {
        return id;
    }

    public CloudUser getCloudUser()
    {
        return cloudUser;
    }

    public User getPanvaUser()
    {
        return panvaUser;
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

    public void setCloudUser(CloudUser cloudUser)
    {
        this.cloudUser = cloudUser;
    }

    public void setPanvaUser(User panvaUser)
    {
        this.panvaUser = panvaUser;
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
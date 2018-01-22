package com.donno.nj.domain;


import java.util.Date;


public class CloudUser
{
    protected Integer id;
    protected String userId;
    protected String password;
    protected String note;
    protected Date createTime;
    protected Date updateTime;

    protected User panvaUser;

    public CloudUser()
    {

    }

    public  Integer getId()
    {
            return id;
    }

    public String getUserId()
    {
        return userId;
    }

    public String getPassword()
    {
        return password;
    }

    public User getPanvaUser()
    {
        return panvaUser;
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

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public void setPanvaUser(User panvaUser)
    {
        this.panvaUser = panvaUser;
    }

    public  void setNote(String note)
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

}

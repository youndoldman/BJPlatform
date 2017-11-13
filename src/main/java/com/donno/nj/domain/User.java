package com.donno.nj.domain;

/**
 * Created by wyb on 2017/10/24.
 */
import java.util.Date;



public class User
{
    protected Integer id;
    protected String userId;
    protected String name;
    protected String password;
    protected String note;
    protected Date createTime;
    protected Date updateTime;

    protected Group userGroup;

    public User()
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

    public String getName()
    {
        return name;
    }

    public String getPassword()
    {
        return password;
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

    public Group getUserGroup()
    {
        return  userGroup;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setPassword(String password)
    {
        this.password = password;
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

    public void setUserGroup(Group userGroup)
    {
        this.userGroup = userGroup;
    }
}

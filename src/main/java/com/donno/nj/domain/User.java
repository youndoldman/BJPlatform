package com.donno.nj.domain;

/**
 * Created by wyb on 2017/10/24.
 */
import java.util.Date;



public class User
{
    protected Integer id;
    protected String name;
    protected String password;
    protected String note;
    protected Date createTime;
    protected Date updateTime;

    protected UserType userType; //用户类型，0 客户 1 系统用户

    public User()
    {

    }

    public  Integer getId()
    {
            return id;
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

    public UserType getUserType()
    {
        return  userType;
    }

    public void setId(Integer id)
    {
        this.id = id;
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

    public void setUserType(UserType userType)
    {
        this.userType = userType;
    }
}

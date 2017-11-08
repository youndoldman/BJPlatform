package com.donno.nj.domain;

import java.io.Serializable;

/**
 * Created by T470P on 2017/10/24.
 */
import com.google.common.base.MoreObjects;
import java.io.Serializable;
import java.util.Date;
public class SysUser extends User implements Serializable
{

    private String userId;
    private String jobNumber;
    private Integer gender;
    private Group group;
    private String mobilePhone;
    private String officePhone;
    private String email;



    public SysUser()
    {
        setUserType(UserType.UTSysUser);
    }

    public  String  getUserId()
    {
        return  userId;
    }

    public String getJobNumber()
    {
        return jobNumber;
    }

    public Integer getGender()
    {
        return gender;
    }

    public Group getGroup()
    {
        return group;
    }

    public String getMobilePhone()
    {
        return mobilePhone;
    }

    public String getOfficePhone()
    {
        return officePhone;
    }

    public String getEmail()
    {
        return email;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public void setJobNumber(String jobNumber)
    {
        this.jobNumber = jobNumber;
    }

    public void setGender(Integer gender)
    {
        this.gender = gender;
    }


    public void setGroup(Group group)
    {
        this.group = group;
    }

    public void setMobilePhone(String mobilePhone)
    {
        this.mobilePhone = mobilePhone;
    }

    public void setOfficePhone(String officePhone)
    {
        this.officePhone = officePhone;
    }

    public  void setEmail(String  email)
    {
        this.email = email;
    }


    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("userId", userId)
                .add("name", name)
                .add("gender", gender)
                .add("jobNumber", jobNumber)
                .add("password", password)
                .add("group", group)
                .add("mobilePhone", mobilePhone)
                .add("officePhone", officePhone)
                .add("email", email)
                .add("createTime", createTime)
                .add("updateTime", updateTime)
                .toString();
    }
}

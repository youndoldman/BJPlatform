package com.donno.nj.domain;

import java.io.Serializable;
import java.util.Date;
import com.google.common.base.MoreObjects;

public class SysUser extends User implements Serializable
{
    private Department department;
    private UserPosition userPosition;
    private String jobNumber;
    private String mobilePhone;
    private String officePhone;
    private String email;
    private AliveStatus aliveStatus;
    private Date aliveUpdateTime;

    public SysUser()
    {

    }

    public Department getDepartment()
    {
        return  department;
    }

    public UserPosition getUserPosition()
    {
        return userPosition;
    }

    public String getJobNumber()
    {
        return jobNumber;
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

    public AliveStatus getAliveStatus()
    {
        return aliveStatus;
    }

    public Date getAliveUpdateTime()
    {
        return aliveUpdateTime;
    }

    public void setDepartment(Department department)
    {
        this.department = department;
    }

    public void setUserPosition(UserPosition userPosition)
    {
        this.userPosition = userPosition;
    }

    public void setJobNumber(String jobNumber)
    {
        this.jobNumber = jobNumber;
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

    public void  setAliveStatus(AliveStatus aliveStatus)
    {
        this.aliveStatus = aliveStatus;
    }

    public void setAliveUpdateTime(Date aliveUpdateTime)
    {
        this.aliveUpdateTime = aliveUpdateTime;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("userId", userId)
                .add("name", name)
                .add("jobNumber", jobNumber)
                .add("password", password)
                .add("group", userGroup)
                .add("mobilePhone", mobilePhone)
                .add("officePhone", officePhone)
                .add("email", email)
                .add("createTime", createTime)
                .add("updateTime", updateTime)
                .toString();
    }
}

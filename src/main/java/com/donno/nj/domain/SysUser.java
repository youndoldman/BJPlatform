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
    private Department department;
    private String jobNumber;
    private String mobilePhone;
    private String officePhone;
    private String email;

    public SysUser()
    {

    }

    public Department getDepartment()
    {
        return  department;
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

    public void setDepartment(Department department)
    {
        this.department = department;
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

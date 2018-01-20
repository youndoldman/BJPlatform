package com.donno.nj.domain;


import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Department  implements Serializable
{
    private Integer id;
    private String code;
    private String name;
    private Department parentDepartment;

    private List<Department> lstSubDepartment;
    private String note;
    private Date createTime;
    private Date updateTime;

    public Department()
    {
    }

    public Integer getId()
    {
        return id;
    }

    public String getCode()
    {
        return code;
    }

    public String  getName()
    {
        return name;
    }

    public Department getParentDepartment()
    {
        return  parentDepartment;
    }

    public List<Department> getLstSubDepartment()
    {
        return lstSubDepartment;
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

    public void setCode(String code)
    {
        this.code = code;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setParentDepartment(Department parentDepartment)
    {
        this.parentDepartment = parentDepartment;
    }

    public void setLstSubDepartment(List<Department> lstSubDepartment)
    {
        this.lstSubDepartment = lstSubDepartment;
    }

    public void  setNote(String note)
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

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("note", note)
                .add("createTime", createTime)
                .add("updateTime", updateTime)
                .toString();
    }
}

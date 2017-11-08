package com.donno.nj.domain;
import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Date;
/*
* @brief 基础数据编码定义
* @detail 基础数据编码定义,编码信息、含义等
* */
public class BaseCodeInfo implements Serializable
{
    protected Integer id;
    protected String  code;
    protected String  name;
    protected String  note;
    protected Date createTime;
    protected Date updateTime;

    /*
    * @brief 构造函数
    *
    * */
    public BaseCodeInfo()
    {
    }

    /*
    * @brief 属性读取，索引列
    *
    * */
    public Integer getId()
    {
        return id;
    }

    /*
    * @brief 属性读取，编码值
    *
    * */
    public String getCode()
    {
        return code;
    }

    public String getName()
    {
        return name;
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

    public void setNote(String Note)
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
                .add("code",code)
                .add("note", note)
                .add("createTime", createTime)
                .add("updateTime", updateTime)
                .toString();
    }

}

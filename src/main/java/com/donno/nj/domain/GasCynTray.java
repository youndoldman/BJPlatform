package com.donno.nj.domain;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Date;

public class GasCynTray implements Serializable
{
    private Integer id;
    private String number;

    DeviceStatus deviceStatus;

    private String  note;
    private Date createTime;
    private Date updateTime;

    public GasCynTray()
    {
    }




    /*
    * 属性读取
    * */

    public Integer getId()
    {
        return id;
    }


    public String getNumber() {
        return number;
    }

    public DeviceStatus getDeviceStatus() {
        return deviceStatus;
    }

    public  String getNote()
    {
        return  note;
    }

    public  Date getCreateTime()
    {
        return  createTime;
    }

    public  Date getUpdateTime()
    {
        return  updateTime;
    }

    /*
    * 属性设置
    * */


    public void setId(Integer id)
    {
        this.id = id;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setDeviceStatus(DeviceStatus deviceStatus) {
        this.deviceStatus = deviceStatus;
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
                .toString();
    }
}
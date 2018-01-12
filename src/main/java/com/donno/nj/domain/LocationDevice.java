package com.donno.nj.domain;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Date;

public class LocationDevice implements Serializable
{
    private Integer id;
    private String number;

    private Double longitude;
    private Double latitude;
    private DeviceStatus status;

    private Float electricQuantity;

    private String  note;
    private Date createTime;
    private Date updateTime;

    public LocationDevice()
    {
    }

    public Integer getId()
    {
        return id;
    }

    public String getNumber()
    {
        return number;
    }

    public Double getLongitude()
    {
        return longitude;
    }

    public Double getLatitude()
    {
        return latitude;
    }

   public DeviceStatus getStatus()
   {
       return  status;
   }

    public Float getElectricQuantity()
    {
        return electricQuantity;
    }

    public String getNote()
    {
        return note;
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

    public void setNumber(String number)
    {
        this.number = number;
    }


    public void setLongitude(Double longitude)
    {
        this.longitude = longitude;
    }

    public void setLatitude(Double latitude)
    {
        this.latitude = latitude;
    }

    public void setStatus(DeviceStatus status)
    {
        this.status = status;
    }

    public void setElectricQuantity(Float electricQuantity)
    {
        this.electricQuantity = electricQuantity;
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
                .add("number", number)
                .add("note", note)
                .add("createTime", createTime)
                .add("updateTime", updateTime)
                .toString();
    }
}
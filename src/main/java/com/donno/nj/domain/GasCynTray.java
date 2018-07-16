package com.donno.nj.domain;

import com.google.common.base.MoreObjects;
import org.apache.xmlgraphics.xmp.schemas.DublinCoreAdapter;

import java.io.Serializable;
import java.util.Date;

public class GasCynTray implements Serializable
{
    private Integer id;
    private String number;

    DeviceStatus deviceStatus;
    WarnningStatus warnningStatus;
    WarnningStatus leakStatus;
    Float weight;
    String timestamp;
    Double longitude;
    Double latitude;

    User user;

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

    public DeviceStatus getDeviceStatus()
    {
        return deviceStatus;
    }

    public WarnningStatus getWarnningStatus()
    {
        return warnningStatus;
    }

    public WarnningStatus getLeakStatus() {
        return leakStatus;
    }

    public User getUser() {
        return user;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Float getWeight() {
        return weight;
    }

    public String getTimestamp() {
        return timestamp;
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

    public void setWarnningStatus(WarnningStatus warnningStatus)
    {
        this.warnningStatus = warnningStatus;
    }

    public void setLeakStatus(WarnningStatus leakStatus)
    {
        this.leakStatus = leakStatus;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
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
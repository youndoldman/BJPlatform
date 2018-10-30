package com.donno.nj.domain;
import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Date;


public class SystemParam implements Serializable
{
    protected Integer id;
    protected Integer aliveExpireTime;
    protected Integer aliveCheckTime;
    protected Integer pushOrderRange;
    protected Integer trayWarningWeight;
    protected Integer  orderOvertime;//订单派送超时时长
    protected String  note;
    protected Date createTime;
    protected Date updateTime;

    /*
    * @brief 构造函数
    *
    * */
    public SystemParam()
    {
    }


    public Integer getId()
    {
        return id;
    }


    public Integer getAliveExpireTime()
    {
        return aliveExpireTime;
    }

    public Integer getAliveCheckTime()
    {
        return aliveCheckTime;
    }

    public Integer getPushOrderRange()
    {
        return pushOrderRange;
    }

    public Integer getTrayWarningWeight() {
        return trayWarningWeight;
    }

    public Integer getOrderOvertime() {
        return orderOvertime;
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

    public void setAliveExpireTime(Integer aliveExpireTime)
    {
        this.aliveExpireTime = aliveExpireTime;
    }

    public void setAliveCheckTime(Integer aliveCheckTime)
    {
        this.aliveCheckTime = aliveCheckTime;
    }

    public void  setPushOrderRange(Integer pushOrderRange)
    {
        this.pushOrderRange = pushOrderRange;
    }

    public void setTrayWarningWeight(Integer trayWarningWeight) {
        this.trayWarningWeight = trayWarningWeight;
    }

    public void setOrderOvertime(Integer orderOvertime) {
        this.orderOvertime = orderOvertime;
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
                .add("note", note)
                .add("createTime", createTime)
                .add("updateTime", updateTime)
                .toString();
    }

}

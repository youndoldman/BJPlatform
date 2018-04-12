package com.donno.nj.domain;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Date;

public class Mend implements Serializable
{
    private Integer id;
    private String  mendSn;//报修单编号
    private MendType mendType;//报修类型
    Customer customer;//报修人

    private String recvName;//收货人名称
    private String recvPhone;//收货人电话
    private CustomerAddress recvAddr; //报修地址
    private String  detail;//报修详情
    private Date  reserveTime;//预约时间


    private EProcessStatus eProcessStatus;//流程状态

    private Department department;//责任部门
    private SysUser dealedUser;//处理人
    private String resloveInfo;//处理结论


    private Date createTime;
    private Date updateTime;

    public Mend()
    {
    }

    /*
    * 属性读取
    * */

    public Integer getId()
    {
        return id;
    }
    public String getMendSn()
    {
        return mendSn;
    }
    public MendType getMendType()
    {
        return mendType;
    }
    public Customer getCustomer()
    {
        return customer;
    }

    public String getRecvName()
    {
        return recvName;
    }
    public String getRecvPhone()
    {
        return recvPhone;
    }
    public CustomerAddress getRecvAddr()
    {
        return recvAddr;
    }
    public String getDetail()
    {
        return detail;
    }
    public Date getReserveTime()
    {
        return reserveTime;
    }


    public EProcessStatus getProcessStatus()
    {
        return eProcessStatus;
    }

    public  Department getDepartment()
    {
        return  department;
    }
    public  SysUser getDealedUser()
    {
        return  dealedUser;
    }
    public  String getresloveInfo()
    {
        return  resloveInfo;
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
    public void setMendSn(String mendSn)
    {
        this.mendSn = mendSn;
    }
    public  void setMendType(MendType mendType)
    {
        this.mendType = mendType;
    }
    public void setCustomer(Customer customer)
    {
        this.customer = customer;
    }


    public void setRecvName(String recvName)
    {
        this.recvName = recvName;
    }
    public void setRecvPhone(String recvPhone)
    {
        this.recvPhone = recvPhone;
    }
    public void setRecvAddr(CustomerAddress recvAddr)
    {
        this.recvAddr = recvAddr;
    }
    public void setDetail(String detail)
    {
        this.detail = detail;
    }
    public void setReserveTime(Date reserveTime)
    {
        this.reserveTime = reserveTime;
    }



    public void setProcessStatus(EProcessStatus eProcessStatus)
    {
        this.eProcessStatus = eProcessStatus;
    }
    public void setDepartment(Department department)
    {
        this.department = department;
    }
    public void setDealedUser(SysUser sysUser)
    {
        this.dealedUser = sysUser;
    }
    public void setResloveInfo(String resloveInfo)
    {
        this.resloveInfo = resloveInfo;
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
                .add("mendSn", mendSn)
                .add("mendType",mendType)
                .add("customer",customer)
                .add("recvName", recvName)
                .add("recvPhone", recvPhone)
                .add("recvAddr", recvAddr)
                .add("detail", detail)
                .add("reserveTime", reserveTime)
                .add("eProcessStatus", eProcessStatus)
                .add("department", department)
                .add("dealedUser", dealedUser)
                .add("createTime", createTime)
                .add("updateTime", updateTime)
                .add("resloveInfo", resloveInfo)
                .toString();
    }
}
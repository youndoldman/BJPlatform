package com.donno.nj.domain;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/*气票*/
public class Ticket implements Serializable
{
    private Integer id;
    private String ticketSn;
    private User customer;  //客户
//    private GasCylinderSpec spec;

    private String specCode;
    private String specName;
    private Float price;
    private User operator;  //操作员
    private TicketStatus  ticketStatus;
    private Date startDate;  //有效期
    private Date endDate;   //有效期
    private Date useTime;  //气票使用日期


    protected String  note;
    protected Date createTime;
    protected Date updateTime;


    public Ticket()
    {
    }

    public Integer getId()
    {
        return id;
    }

    public String getTicketSn() {
        return ticketSn;
    }

    public User getCustomer() {
        return customer;
    }

//    public GasCylinderSpec getSpec()
//    {
//        return  spec;
//    }


    public String getSpecCode() {
        return specCode;
    }

    public String getSpecName() {
        return specName;
    }

    public TicketStatus getTicketStatus()
    {
        return ticketStatus;
    }


    public Float getPrice()
    {
        return  price;
    }

    public User getOperator() {
        return operator;
    }

    public Date getStartDate()
    {
        return startDate;
    }

    public Date getEndDate()
    {
        return  endDate;
    }

    public Date getUseTime()
    {
        return useTime;
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

    public void setTicketSn(String ticketSn) {
        this.ticketSn = ticketSn;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

//    public void setSpec(GasCylinderSpec spec)
//    {
//        this.spec = spec;
//    }


    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public void setSpecCode(String specCode) {
        this.specCode = specCode;
    }

    public void setTicketStatus(TicketStatus ticketStatus)
    {
        this.ticketStatus = ticketStatus;
    }

    public void setPrice(Float price)
    {
        this.price = price;
    }

    public void setOperator(User operator) {
        this.operator = operator;
    }

    public  void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }

    public void setUseTime(Date useTime) {
        this.useTime = useTime;
    }

    public void setNote(String note)
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
                .toString();
    }
}
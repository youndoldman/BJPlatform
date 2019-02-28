
/**
 * @file Customer.java
 * @brief  客户实体信息
 * @author wyb
 * @date  2017/11/25 0025 下午 3:02
 * @version  1.00
 * <pre>company: CETC28</pre>
 * <pre><b>All rights reserved.</b></pre>
 */

package com.donno.nj.domain;
import com.donno.nj.service.CustomerService;
import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * @class Customer
 * @brief   客户实体信息类
 */
public class Customer extends User  implements Serializable
{
    private String number;        /**< 客户编号*/
    private boolean haveCylinder; /**<是否携瓶*/
    private Integer status;       /**<客户状态*/

    private SettlementType settlementType;  /*结算类型*/
    private CustomerType customerType;       /**<客户类型*/
    private CustomerLevel customerLevel;     /**<客户级别*/
    private CustomerSource customerSource;   /**<客户来源*/
    private CustomerCompany customerCompany;   /**<客户公司*/

    private CustomerAddress address;   /**<客户地址信息，只能修改，不能删除*/
    private String phone;              /**<客户电话信息*/

    private Integer sleepDays;//沉睡天数


    private Date leakLevelOneWanningTime;/*一级报警时间 */
    private Date leakLevelTwoWanningTime;/*二级报警时间 */


    /**
     * @fn   Customer
     * @brief 构造函数
     */
    public Customer()
    {
    }

    /**
     * @fn getNumber
     * @brief 取客户编号
     * @return  String 客户编号
     */
    public String getNumber()
    {
        return number;
    }

    /**
     * @fn   getHaveCylinder
     * @brief 取客户携瓶入网状态信息
     * @return boolean 是否携瓶入网
     */
    public boolean getHaveCylinder()
    {
        return haveCylinder;
    }

    /**
     * @fn   getStatus
     * @brief 取客户状态
     * @return Integer  客户状态
     */
    public Integer getStatus()
    {
        return status;
    }

    public SettlementType getSettlementType()
    {
        return settlementType;
    }

    /**
     * @fn   getCustomerType
     * @brief 取客户类型信息
     * @return CustomerType  客户类型
     */
    public CustomerType getCustomerType()
    {
        return customerType;
    }

    /**
     * @fn   getCustomerLevel
     * @brief 取客户级别信息
     * @return String  客户级别信息
     */
    public CustomerLevel getCustomerLevel()
    {
        return customerLevel;
    }

    /**
     * @fn   getCustomerSource
     * @brief 取客户来源信息
     * @return String  客户来源信息
     */
    public CustomerSource  getCustomerSource()
    {
        return customerSource;
    }

    /**
     * @fn   getCustomerCompany
     * @brief 取客户公司信息
     * @return String  客户公司信息
     */
    public CustomerCompany getCustomerCompany()
    {
        return customerCompany;
    }


    /**
     * @fn   getAddress
     * @brief 取客户地址信息
     * @return String  客户来源编码
     */
    public CustomerAddress getAddress()
    {
        return address;
    }

    public Integer getSleepDays() {
        return sleepDays;
    }

    /**
     * @fn   getPhone1
     * @brief 取客户电话信息
     * @return String  客户电话
     */
    public String getPhone()
    {
        return phone;
    }


    public Date getLeakLevelOneWanningTime()
    {
        return leakLevelOneWanningTime;
    }

    public Date getLeakLevelTwoWanningTime()
    {
        return leakLevelTwoWanningTime;
    }



    /**
     * @fn  setNumber
     * @brief    设置客户编号信息
     * @param[in]  number 客户编号
     */
    public void setNumber(String number)
    {
        this.number = number;
    }

    /**
     * @fn  setNumber
     * @brief    设置客户携瓶状态
     * @param[in]  haveCylinder 携瓶状态
     */
    public void setHaveCylinder(boolean haveCylinder)
    {
        this.haveCylinder = haveCylinder;
    }

    /**
     * @fn  setStatus
     * @brief    设置客户状态
     * @param[in]  status 客户状态
     */
    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public void setSettlementType(SettlementType settlementType){this.settlementType = settlementType;}

    /**
     * @fn  setCustomerType
     * @brief    设置客户类型
     * @param[in]  customerType 客户类型
     */
    public void setCustomerType(CustomerType customerType)
    {
        this.customerType = customerType;
    }

    /**
     * @fn  setCustomerLevel
     * @brief    设置客户级别
     * @param[in]  customerLevel 客户级别
     */
    public void setCustomerLevel(CustomerLevel customerLevel)
    {
        this.customerLevel = customerLevel;
    }

    /**
     * @fn  setCustomerSource
     * @brief    设置客户来源
     * @param[in]  customerSource 客户来源
     */
    public void setCustomerSource(CustomerSource customerSource)
    {
        this.customerSource = customerSource;
    }

    public void setCustomerCompany(CustomerCompany customerCompany)
    {
        this.customerCompany = customerCompany;
    }

//    public void setDistrictCode(String districtCode)
//    {
//        this.districtCode = districtCode;
//    }
//
//    public void setGasStoreCode(String gasStoreCode)
//    {
//        this.gasStoreCode = gasStoreCode;
//    }


    public void setSleepDays(Integer sleepDays) {
        this.sleepDays = sleepDays;
    }

    /**
     * @fn  setAddress
     * @brief    设置客户地址
     * @param[in]  address 客户地址
     */
    public void setAddress(CustomerAddress address)
    {
        this.address = address;
    }

    /**
     * @fn  setPhone
     * @brief    设置客户电话
     * @param[in]  phone 客户电话
     */
    public  void setPhone(String phone)
    {
        this.phone = phone;
    }



    public void setLeakLevelOneWarningTime(Date leakLevelOneWanningTime)
    {
        this.leakLevelOneWanningTime = leakLevelOneWanningTime;
    }

    public void setLeakLevelTwoWarningTime(Date leakLevelTwoWanningTime)
    {
        this.leakLevelTwoWanningTime = leakLevelTwoWanningTime;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("userId", userId)
                .add("number",number)
                .add("name", name)
                .add("password",password)
                .add("status",status)
                .add("address",address)
                .add("phone", phone)
                .add("note", note)
                .add("leakLevelOneWanningTime", leakLevelOneWanningTime)
                .add("leakLevelTwoWanningTime", leakLevelTwoWanningTime)
                .add("createTime", createTime)
                .add("updateTime", updateTime)
                .toString();
    }
}
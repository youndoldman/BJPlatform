package com.donno.nj.service.impl;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.dao.*;
import com.donno.nj.domain.*;
import com.donno.nj.service.CustomerService;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.donno.nj.exception.ServerSideBusinessException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class CustomerServiceImpl extends UserServiceImpl implements CustomerService
{

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private CustomerSourceDao customerSourceDao;

    @Autowired
    private SettlementTypeDao settlementTypeDao;

    @Autowired
    private CustomerTypeDao customerTypeDao;

    @Autowired
    private CustomerLevelDao customerLevelDao;

    @Autowired
    private CustomerCompanyDao customerCompanyDao;

    @Autowired
    private OrderDao orderDao;


    @Autowired
    private UserPositionDao userPositionDao;

    @Override
    @OperationLog(desc = "查询客户信息")
    public List<Customer> retrieve(Map params)
    {
        List<Customer> customers = customerDao.getList(params);
        return customers;
    }

    @Override
    @OperationLog(desc = "查询客户数量")
    public Integer count(Map params) {
        return customerDao.count(params);
    }

    @Override
    @OperationLog(desc = "创建客户信息")
    public void create(Customer customer)
    {
        User user = customer;

        /*校验用户是否存在*/
        checkUserExist(customer.getUserId());

        /*用户组信息校验*/
        setCustomerGroup(customer);

        /*客户地址信息*/
        checkAddress(customer);

        /*客户来源*/
        checkCustomerSource(customer);

        /*客户级别*/
        checkCustomerLevel(customer);

        /*结算类型*/
        checkSettlement(customer);

        /*客户类型*/
        checkCustomerType(customer);

        /*客户公司*/
        checkCustomerCompany(customer);

        /*自动生成客户编号NUMBER*/
        customer.setNumber(customer.getUserId());

        userDao.insert(user);//插入用户基表数据，自动返回id值到user
        customerDao.insert(customer);//插入客户表数据，

    }

    @Override
    @OperationLog(desc = "修改客户信息")
    public void update(Integer id, Customer newCustomer)
    {
        newCustomer.setId(id);

        /*校验源信息是否合法*/
        User oldCustomer = userDao.findById(id);
        if ( oldCustomer == null)
        {
            throw new ServerSideBusinessException("用户不存在，不能进行修改操作！",HttpStatus.CONFLICT);
        }

        /*校验目的用户是否已经存在*/
        if (newCustomer.getUserId() != null )
        {
            if ( newCustomer.getUserId().equals(oldCustomer.getUserId()) )//userid 不修改
            {
                newCustomer.setUserId(null);
            }
            else
            {
                checkUserExist(newCustomer.getUserId());
            }
        }

        /*用户组不允许修改*/
        if (newCustomer.getUserGroup() != null)
        {
            newCustomer.setUserGroup(null);
        }

       /*客户地址信息*/
        if (newCustomer.getAddress() != null)
        {
            checkAddress(newCustomer);
        }

        /*结算类型信息*/
        if (newCustomer.getSettlementType() != null)
        {
            checkSettlement(newCustomer);
        }

        /*客户来源*/
        if (newCustomer.getCustomerSource() != null)
        {
            checkCustomerSourceUpdate(newCustomer);
        }


        /*客户级别*/
        if (newCustomer.getCustomerLevel() != null)
        {
            checkCustomerLevelUpdate(newCustomer);
        }


        /*客户类型*/
        if (newCustomer.getCustomerType() != null)
        {
            checkCustomerTypeUpdate(newCustomer);
        }


        /*客户公司*/
        if (newCustomer.getCustomerCompany() != null)
        {
            checkCustomerCompanyUpdate(newCustomer);
        }


        /*更新基表数据*/
        User newUser = newCustomer;
        userDao.update(newUser);

        /*更新客户表数据*/
        customerDao.update(newCustomer);
    }

    @Override
    @OperationLog(desc = "删除客户")
    public void deleteById(Integer id)
    {
        Customer customer = customerDao.findById(id);
        if (customer == null)
        {
            throw new ServerSideBusinessException("用户不存在，不能进行删除操作！",HttpStatus.NOT_FOUND);
        }

        /*检查，如果有订单记录，则不允许删除*/
        Map params = new HashMap<String,String>();
        params.putAll(ImmutableMap.of("userId", customer.getUserId()));
        if (orderDao.count(params) > 0)
        {
            throw new ServerSideBusinessException("客户已有订单记录，不能删除客户信息！",HttpStatus.FORBIDDEN);
        }

        /*删除位置信息表*/
        userPositionDao.deleteByUserId(customer.getUserId());

        /*删除主表*/
        userDao.delete(id);
        customerDao.deleteByUserIdx(id);
    }


    public void checkAddress(Customer customer)
    {
        if (customer.getAddress() == null)
        {
            throw new ServerSideBusinessException("请填写用户地址信息",HttpStatus.NOT_ACCEPTABLE);
        }
        else
        {
            if(! ((customer.getAddress().getProvince() != null && customer.getAddress().getProvince().trim().length() != 0 ) &&
                 (customer.getAddress().getCity() != null && customer.getAddress().getCity().trim().length() != 0 ) &&
                 (customer.getAddress().getCounty() != null   && customer.getAddress().getCounty().trim().length() != 0 ) &&
                 (customer.getAddress().getDetail() != null && customer.getAddress().getDetail().trim().length() != 0))
                    )
            {
                throw new ServerSideBusinessException("请填写用户地址信息",HttpStatus.NOT_ACCEPTABLE);
            }
        }
    }

    public void checkCustomerSource(Customer customer)
    {
        CustomerSource customerSource ;
        if (customer.getCustomerSource() == null)//无用户来源信息，则默认置为未定义
        {
            customerSource = customerSourceDao.findByCode(ServerConstantValue.DB_ROW_UNDIFINED_CODE);
            if (customerSource == null)
            {
                customerSource = new CustomerSource();
                customerSource.setCode(ServerConstantValue.DB_ROW_UNDIFINED_CODE);
                customerSource.setName(ServerConstantValue.DB_ROW_UNDIFINED_NAME);
                customerSourceDao.insert(customerSource);
            }
        }
        else
        {
            if (customer.getCustomerSource().getCode() != null &&  customer.getCustomerSource().getCode().trim().length() != 0 )
            {
                customerSource = customerSourceDao.findByCode(customer.getCustomerSource().getCode());//用户来源信息错误
                if (customerSource == null)
                {
                    throw new ServerSideBusinessException("客户来源信息错误",HttpStatus.NOT_ACCEPTABLE);
                }
            }
            else
            {
                customerSource = new CustomerSource();
                customerSource.setCode(ServerConstantValue.DB_ROW_UNDIFINED_CODE);
                customerSource.setName(ServerConstantValue.DB_ROW_UNDIFINED_NAME);
                customerSourceDao.insert(customerSource);
            }
        }

        customer.setCustomerSource(customerSource);
    }


    public void checkSettlement(Customer customer)
    {
        SettlementType settlementType;
        if (customer.getSettlementType() == null)//用户级别信息为空
        {
            throw new ServerSideBusinessException("缺少结算类型信息",HttpStatus.NOT_ACCEPTABLE);
        }
        else
        {
            if (customer.getSettlementType().getCode() != null &&  customer.getSettlementType().getCode().trim().length() != 0 )
            {
                settlementType = settlementTypeDao.findByCode(customer.getCustomerLevel().getCode());
                if (settlementType == null)
                {
                    throw new ServerSideBusinessException("结算类型信息错误",HttpStatus.NOT_ACCEPTABLE);
                }
            }
            else
            {
                throw new ServerSideBusinessException("结算类型信息错误",HttpStatus.NOT_ACCEPTABLE);
            }
        }

        customer.setSettlementType(settlementType);
    }

    public void checkCustomerLevel(Customer customer)
    {
        CustomerLevel customerLevel;
        if (customer.getCustomerLevel() == null)//用户级别信息为空
        {
            customerLevel = customerLevelDao.findByCode(ServerConstantValue.DB_ROW_UNDIFINED_CODE);
            if (customerLevel == null)
            {
                customerLevel = new CustomerLevel();
                customerLevel.setCode(ServerConstantValue.DB_ROW_UNDIFINED_CODE);
                customerLevel.setName(ServerConstantValue.DB_ROW_UNDIFINED_NAME);
                customerLevelDao.insert(customerLevel);
            }
        }
        else
        {
            if (customer.getCustomerLevel().getCode() != null &&  customer.getCustomerLevel().getCode().trim().length() != 0 )
            {
                customerLevel = customerLevelDao.findByCode(customer.getCustomerLevel().getCode());
                if (customerLevel == null)
                {
                    throw new ServerSideBusinessException("客户级别信息错误",HttpStatus.NOT_ACCEPTABLE);
                }
            }
            else
            {
                customerLevel = new CustomerLevel();
                customerLevel.setCode(ServerConstantValue.DB_ROW_UNDIFINED_CODE);
                customerLevel.setName(ServerConstantValue.DB_ROW_UNDIFINED_NAME);
                customerLevelDao.insert(customerLevel);
            }
        }

        customer.setCustomerLevel(customerLevel);
    }


    public void checkCustomerType(Customer customer)
    {
        CustomerType customerType;
        if (customer.getCustomerType() == null)
        {
            customerType = customerTypeDao.findByCode(ServerConstantValue.DB_ROW_UNDIFINED_CODE);
            if (customerType == null)
            {
                customerType = new CustomerType();
                customerType.setCode(ServerConstantValue.DB_ROW_UNDIFINED_CODE);
                customerType.setName(ServerConstantValue.DB_ROW_UNDIFINED_NAME);
                customerTypeDao.insert(customerType);
            }
        }
        else
        {
            if (customer.getCustomerType().getCode() != null &&  customer.getCustomerType().getCode().trim().length() != 0 )
            {
                customerType = customerTypeDao.findByCode(customer.getCustomerType().getCode());
                if (customerType == null)
                {
                    throw new ServerSideBusinessException("客户级别信息错误",HttpStatus.NOT_ACCEPTABLE);
                }
            }
            else
            {
                customerType = new CustomerType();
                customerType.setCode(ServerConstantValue.DB_ROW_UNDIFINED_CODE);
                customerType.setName(ServerConstantValue.DB_ROW_UNDIFINED_NAME);
                customerTypeDao.insert(customerType);
            }
        }

        customer.setCustomerType(customerType);
    }


    public void checkCustomerCompany(Customer customer)
    {
        CustomerCompany customerCompany;
        if (customer.getCustomerCompany() == null)//没有客户公司信息
        {
            customerCompany = customerCompanyDao.findByCode(ServerConstantValue.DB_ROW_UNDIFINED_CODE);
            if (customerCompany == null) {
                customerCompany = new CustomerCompany();
                customerCompany.setCode(ServerConstantValue.DB_ROW_UNDIFINED_CODE);
                customerCompany.setName(ServerConstantValue.DB_ROW_UNDIFINED_NAME);
                customerCompanyDao.insert(customerCompany);
            }
        }
        else
        {
            if (customer.getCustomerCompany().getCode() != null &&  customer.getCustomerCompany().getCode().trim().length() != 0)
            {
                customerCompany = customerCompanyDao.findByCode(customer.getCustomerCompany().getCode());
                if (customerCompany == null)
                {
                    throw new ServerSideBusinessException("客户公司信息错误，找不到指定的公司信息",HttpStatus.NOT_ACCEPTABLE);
                }
            }
            else//只有公司名称，查找是否存在该公司，若不存在则添加到公司表中
            {
                if (customer.getCustomerCompany().getName() != null && customer.getCustomerCompany().getName().trim().length() != 0 )
                {
                    customerCompany = customerCompanyDao.findByName(customer.getCustomerCompany().getName());
                    if(customerCompany == null)
                    {
                        customerCompany = customer.getCustomerCompany();
                        customerCompany.setCode( customerCompanyDao.getNextCode());
                        customerCompanyDao.insert(customerCompany);
                    }
                }
                else
                {
                    throw new ServerSideBusinessException("客户公司信息错误,缺少公司名称",HttpStatus.NOT_ACCEPTABLE);
                }
            }
        }

        customer.setCustomerCompany(customerCompany);
    }



    public void checkCustomerSourceUpdate(Customer customer)
    {
        if (customer.getCustomerSource() != null)
        {
            if (customer.getCustomerSource().getCode() != null &&  customer.getCustomerSource().getCode().trim().length() != 0 )
            {
                CustomerSource customerSource = customerSourceDao.findByCode(customer.getCustomerSource().getCode());
                if (  customerSource == null )
                {
                    throw new ServerSideBusinessException("客户来源信息错误",HttpStatus.NOT_ACCEPTABLE);
                }
                else
                {
                    customer.setCustomerSource(customerSource);
                }
            }
            else
            {
                throw new ServerSideBusinessException("客户来源信息错误",HttpStatus.NOT_ACCEPTABLE);
            }
        }

    }

    public void checkCustomerLevelUpdate(Customer customer)
    {
        if (customer.getCustomerLevel() != null)
        {
            if (customer.getCustomerLevel().getCode() != null &&  customer.getCustomerLevel().getCode().trim().length() != 0 )
            {
                CustomerLevel customerLevel = customerLevelDao.findByCode(customer.getCustomerLevel().getCode());
                if (  customerLevel == null )
                {
                    throw new ServerSideBusinessException("客户级别信息错误",HttpStatus.NOT_ACCEPTABLE);
                }
                else
                {
                    customer.setCustomerLevel(customerLevel);
                }
            }
            else
            {
                throw new ServerSideBusinessException("客户级别信息错误",HttpStatus.NOT_ACCEPTABLE);
            }
        }
    }


    public void checkCustomerTypeUpdate(Customer customer)
    {
        if (customer.getCustomerType() != null)
        {
            if (customer.getCustomerType().getCode() != null &&  customer.getCustomerType().getCode().trim().length() != 0 )
            {
                CustomerType customerType = customerTypeDao.findByCode(customer.getCustomerType().getCode());
                if (  customerType == null )
                {
                    throw new ServerSideBusinessException("客户类型信息错误",HttpStatus.NOT_ACCEPTABLE);
                }
                else
                {
                    customer.setCustomerType(customerType);
                }
            }
            else
            {
                throw new ServerSideBusinessException("客户类型信息错误",HttpStatus.NOT_ACCEPTABLE);
            }
        }
    }


    public void checkCustomerCompanyUpdate(Customer customer)
    {
        if (customer.getCustomerCompany() != null)
        {
            CustomerCompany customerCompany ;

            if (customer.getCustomerCompany().getCode() != null &&  customer.getCustomerCompany().getCode().trim().length() != 0 )
            {
                customerCompany = customerCompanyDao.findByCode(customer.getCustomerCompany().getCode());
                if (  customerCompany == null )
                {
                    throw new ServerSideBusinessException("客户公司信息错误",HttpStatus.NOT_ACCEPTABLE);
                }
            }
            else
            {
                if (customer.getCustomerCompany().getName() != null && customer.getCustomerCompany().getName().trim().length() != 0 )
                {
                    customerCompany = customerCompanyDao.findByName(customer.getCustomerCompany().getName());
                    if(customerCompany == null)
                    {
                        customerCompany = customer.getCustomerCompany();
                        customerCompany.setCode( customerCompanyDao.getNextCode());
                        customerCompanyDao.insert(customerCompany);
                    }
                }
                else
                {
                    throw new ServerSideBusinessException("客户公司信息错误,缺少公司名称",HttpStatus.NOT_ACCEPTABLE);
                }
            }
            customer.setCustomerCompany(customerCompany);
        }
    }

    public void setCustomerGroup(Customer customer)
    {
        //查找客户组
       Group group = groupDao.findByCode("00004");
        if (group != null)
        {
            customer.setUserGroup(group);
        }
        else
        {
            throw new ServerSideBusinessException("系统缺少客户组信息，无法创建客户",HttpStatus.NOT_ACCEPTABLE);
        }
    }




}


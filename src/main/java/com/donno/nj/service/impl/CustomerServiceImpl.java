package com.donno.nj.service.impl;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.dao.CustomerAddressDao;
import com.donno.nj.dao.CustomerDao;
import com.donno.nj.dao.UserDao;
import com.donno.nj.dao.GroupDao;
import com.donno.nj.domain.Customer;
import com.donno.nj.domain.CustomerAddress;
import com.donno.nj.domain.User;
import com.donno.nj.domain.Group;
import com.donno.nj.service.CustomerService;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class CustomerServiceImpl extends UserServiceImpl implements CustomerService {

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private CustomerAddressDao customerAddressDao;


    @Override
    @OperationLog(desc = "查询客户信息")
    public List<Customer> retrieve(Map params)
    {
        List<Customer> customers = customerDao.getList(params);
        for(int iCount = 0 ; iCount < customers.size(); iCount++)
        {
            CustomerAddress customerAddress =  customerAddressDao.findByCustomerIdx(customers.get(iCount).getId());

            if (customerAddress != null)
            {
                customers.get(iCount).setAddress(customerAddress);
            }
        }
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
        long retCode = userDao.insert(user);//插入用户基表数据，自动返回id值到user

        if (retCode > 0)
        {
            retCode = customerDao.insert(customer);//插入客户表数据，

            if (retCode > 0)
            {
                if (customer.getAddress() != null)
                {
                    customer.getAddress().setCustomerIdx(customer.getId());
                    customerAddressDao.insert(customer.getAddress());  //插入地址信息
                }
            }
        }
    }

    @Override
    @OperationLog(desc = "修改客户信息")
    public void update(Integer id, Customer newCustomer)
    {
        newCustomer.setId(id);

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
        /*删除关联子表：地址表*/
        customerAddressDao.deleteByUserIdx(id);
        userDao.delete(id);
        customerDao.deleteByUserIdx(id);
    }


}

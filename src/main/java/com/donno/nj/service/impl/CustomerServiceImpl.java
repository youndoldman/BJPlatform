package com.donno.nj.service.impl;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.dao.CustomerAddressDao;
import com.donno.nj.dao.CustomerDao;
import com.donno.nj.domain.Customer;
import com.donno.nj.service.CustomerService;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private CustomerAddressDao customerAddressDao;


    @Override
    @OperationLog(desc = "根据用户编号查询客户信息")
    public Optional<Customer> findByNumber(String number) {
        return Optional.fromNullable(customerDao.findByNumber(number));
    }

    @Override
    @OperationLog(desc = "根据用户ID查询客户信息")
    public Optional<Customer> findByUserId(String userIdentity) {
        return Optional.fromNullable(customerDao.findByUserId(userIdentity));
    }

    @Override
    @OperationLog(desc = "查询客户信息")
    public List<Customer> retrieve(Map params) {
        return customerDao.getList(params);
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

        long retCode = customerDao.insert(customer);//插入后自动返回id值到customer

        if (retCode > 0)
        {
            if (customer.getAddress() != null)
            {
                customer.getAddress().setCustomerIdx(customer.getId());
                customerAddressDao.insert(customer.getAddress());
            }
        }
    }

    @Override
    @OperationLog(desc = "修改客户信息")
    public void update(Customer customer, Customer newCustomer) {
        newCustomer.setId(customer.getId());
        customerDao.update(newCustomer);
    }

    @Override
    @OperationLog(desc = "删除客户")
    public void delete(Customer customer)
    {
        /*删除关联子表：地址表*/
        customerAddressDao.deleteByUserId(customer.getId());
        customerDao.deleteByUserId(customer.getUserId());
    }


}

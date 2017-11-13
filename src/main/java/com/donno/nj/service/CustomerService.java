package com.donno.nj.service;

import com.donno.nj.domain.Customer;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
public interface CustomerService extends UserService
{
    List<Customer> retrieve(Map params);

    Integer count(Map params);

    void create(Customer user);

    void update(Integer id, Customer newUser);

    void deleteById(Integer id);

}

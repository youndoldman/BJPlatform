package com.donno.nj.service;

import com.donno.nj.domain.Customer;
import com.google.common.base.Optional;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
public interface CustomerService
{
    Optional<Customer> findByNumber(String number);

    Optional<Customer> findByUserId(String userIdentity);

    List<Customer> retrieve(Map params);

    Integer count(Map params);

    void create(Customer user);

    void update(Customer user, Customer newUser);

    void delete(Customer customer);

}

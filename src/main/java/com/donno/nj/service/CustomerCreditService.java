package com.donno.nj.service;

import com.donno.nj.domain.CustomerCredit;
import com.donno.nj.domain.Ticket;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
public interface CustomerCreditService
{
    List<CustomerCredit> retrieve(Map params);

    Integer count(Map params);

//    void create(CustomerCredit ticket);

//    void update(Integer id, CustomerCredit newCustomerCredit);

    void deleteById(Integer id);

}

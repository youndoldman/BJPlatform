package com.donno.nj.service;

import com.donno.nj.domain.CustomerCredit;
import com.donno.nj.domain.CustomerCreditDetail;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
public interface CustomerCreditDetailService
{
    List<CustomerCreditDetail> retrieve(Map params);

    Integer count(Map params);

    void create(CustomerCreditDetail customerCreditDetail);

    void deleteById(Integer id);

}

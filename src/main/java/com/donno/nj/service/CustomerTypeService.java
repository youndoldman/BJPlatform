package com.donno.nj.service;

import com.donno.nj.domain.CustomerType;
import com.google.common.base.Optional;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
public interface CustomerTypeService
{
    Optional<CustomerType> findByCode(String code);

    List<CustomerType> retrieve(Map params);

    Integer count(Map params);

    CustomerType create(CustomerType customerType);

    void update(CustomerType customerType, CustomerType newCustomerType);

    void delete(CustomerType customerType);
}

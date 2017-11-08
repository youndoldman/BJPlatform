package com.donno.nj.service;

import com.donno.nj.domain.CustomerDistrict;
import com.google.common.base.Optional;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
public interface CustomerDistrictService
{
    Optional<CustomerDistrict> findByCode(String code);

    List<CustomerDistrict> retrieve(Map params);

    Integer count(Map params);

    CustomerDistrict create(CustomerDistrict customerDistrict);

    void update(CustomerDistrict customerDistrict, CustomerDistrict newCustomerDistrict);

    void delete(CustomerDistrict customerDistrict);
}

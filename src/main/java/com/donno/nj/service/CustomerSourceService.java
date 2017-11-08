package com.donno.nj.service;

import com.donno.nj.domain.CustomerSource;
import com.google.common.base.Optional;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by T470P on 2017/10/30.
 */

@Transactional
public interface CustomerSourceService
{
    Optional<CustomerSource> findByCode(String code);

    List<CustomerSource> retrieve(Map params);

    Integer count(Map params);

    CustomerSource create(CustomerSource customerSource);

    void update(CustomerSource customerSource, CustomerSource newCustomerSource);

    void delete(CustomerSource customerType);
}

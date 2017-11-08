package com.donno.nj.service;

import com.donno.nj.domain.CustomerCharacter;
import com.google.common.base.Optional;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by T470P on 2017/10/30.
 */

@Transactional
public interface CustomerCharacterService
{
    Optional<CustomerCharacter> findByCode(String code);

    List<CustomerCharacter> retrieve(Map params);

    Integer count(Map params);

    CustomerCharacter create(CustomerCharacter customerType);

    void update(CustomerCharacter customerType, CustomerCharacter newCustomerType);

    void delete(CustomerCharacter customerType);
}

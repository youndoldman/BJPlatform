package com.donno.nj.service;

import com.donno.nj.domain.CustomerLevel;
import com.google.common.base.Optional;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by T470P on 2017/10/30.
 */

@Transactional
public interface CustomerLevelService
{
    Optional<CustomerLevel> findByCode(String code);

    List<CustomerLevel> retrieve(Map params);

    Integer count(Map params);

    CustomerLevel create(CustomerLevel customerLevel);

    void update(Integer id, CustomerLevel customerLevel);

    void delete(Map params);
}

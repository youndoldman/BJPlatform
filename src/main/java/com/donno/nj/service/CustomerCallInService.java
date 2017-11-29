package com.donno.nj.service;

import com.donno.nj.domain.CustomerCallIn;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
public interface CustomerCallInService
{
    List<CustomerCallIn> retrieve(Map params);

    Integer count(Map params);

    void create(CustomerCallIn user);

    void delete(Map params);

}

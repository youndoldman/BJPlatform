package com.donno.nj.service;

import com.donno.nj.domain.Advice;
import com.donno.nj.domain.ElectDeposit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
public interface ElectDepositService
{
    List<ElectDeposit> retrieve(Map params);

    Integer count(Map params);

    void create(ElectDeposit electDeposit);

//    void update(Integer id, ElectDeposit electDeposit);

    void deleteById(Integer id);

}

package com.donno.nj.service;

import com.donno.nj.domain.DepositDetail;
import com.donno.nj.domain.WriteOffDetail;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
public interface DepositDetailService
{
    List<DepositDetail> retrieve(Map params);

    Integer count(Map params);

    void create(DepositDetail depositDetail);

    void update(Integer id, DepositDetail newDepositDetail);

    void deleteById(Integer id);

}

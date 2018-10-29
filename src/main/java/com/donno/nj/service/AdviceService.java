package com.donno.nj.service;

import com.donno.nj.domain.Advice;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
public interface AdviceService
{
    List<Advice> retrieve(Map params);

    Integer count(Map params);

    void create(Advice advice);

    void update(Integer id,Advice advice);

    void deleteById(Integer id);

}

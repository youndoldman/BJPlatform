package com.donno.nj.service;

import com.donno.nj.domain.CustomerCredit;
import com.donno.nj.domain.WriteOffDetail;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
public interface WriteOffDetailService
{
    List<WriteOffDetail> retrieve(Map params);

    Integer count(Map params);

    void create(WriteOffDetail writeOffDetail);

    void update(Integer id, WriteOffDetail newWriteOffDetail);

    void deleteById(Integer id);

}

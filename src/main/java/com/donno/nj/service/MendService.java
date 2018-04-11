package com.donno.nj.service;

import com.donno.nj.domain.Mend;
import com.google.common.base.Optional;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
public interface MendService
{
    Optional<Mend> findBySn(String sn);

    List<Mend> retrieve(Map params);

    Integer count(Map params);

    void create(Mend mend);

    void update(String sn, Mend newMends);

    void deleteById(Integer id);
}

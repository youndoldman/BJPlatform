package com.donno.nj.service;

import com.donno.nj.domain.Complaint;
import com.google.common.base.Optional;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
public interface ComplaintService
{
    Optional<Complaint> findBySn(String sn);

    List<Complaint> retrieve(Map params);

    Integer count(Map params);

    void create(Complaint complaint);

    void update(String sn, Complaint newComplaint);

    void deleteById(Integer id);
}

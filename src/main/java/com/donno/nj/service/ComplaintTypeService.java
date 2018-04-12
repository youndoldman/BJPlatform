package com.donno.nj.service;

import com.donno.nj.domain.ComplaintType;
import com.google.common.base.Optional;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
public interface ComplaintTypeService
{
    Optional<ComplaintType> findByCode(String code);

    List<ComplaintType> retrieve(Map params);

    Integer count(Map params);

    ComplaintType create(ComplaintType complaintType);

    void update(String code, ComplaintType newComplaintType);

    void deleteByCode(String code);
}

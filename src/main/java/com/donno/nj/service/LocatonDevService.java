package com.donno.nj.service;

import com.donno.nj.domain.LocationDevice;
import com.google.common.base.Optional;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
public interface LocatonDevService
{
    Optional<LocationDevice> findByNumber(String Number);

    List<LocationDevice> retrieve(Map params);

    Integer count(Map params);

    void create(LocationDevice locationDevice);

    void update(String number, LocationDevice newLocationDevice);

    void deleteById(Integer id);
}

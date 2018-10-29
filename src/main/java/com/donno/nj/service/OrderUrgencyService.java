package com.donno.nj.service;

import com.donno.nj.domain.OrderUrgency;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
public interface OrderUrgencyService
{
    List<OrderUrgency> retrieve(Map params);

    Integer count(Map params);

    void create(OrderUrgency orderUrgency);

    void update(Integer id, OrderUrgency orderUrgency);

    void deleteById(Integer id);

}

package com.donno.nj.service;

import com.donno.nj.domain.Order;
import com.google.common.base.Optional;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
public interface OrderService
{
    Optional<Order> findBySn(String sn);

    List<Order> retrieve(Map params);

    Integer count(Map params);

    void create(Order order);

    void update(Integer id, Order newOrder);

    void deleteById(Integer id);
}

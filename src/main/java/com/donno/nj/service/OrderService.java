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

    void update(String taskId,Map<String, Object> variables,Integer id, Order newOrder);//订单任务更新

    void deleteById(Integer id);

    void OrderOperHistory(String orderSn,Integer orderStatus) ;
}

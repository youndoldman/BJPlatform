package com.donno.nj.service;

import com.donno.nj.domain.Order;
import com.donno.nj.domain.Ticket;
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

    void update(Order srcOrder, Order newOrder);

    void ticketPay(Order order,String coupuns,String tickets);

    void update(String taskId,Map<String, Object> variables,Integer id, Order newOrder);//订单任务更新

    void deleteById(Integer id);

    void cancelOrder(String orderSn);

    void OrderOperHistory(Order order,Integer orderStatus) ;

    void weixinPayOk(String orderSn,String weixinSn,Integer amount);


}

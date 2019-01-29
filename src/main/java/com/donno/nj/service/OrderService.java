package com.donno.nj.service;

import com.donno.nj.domain.*;
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

    void orderPay(Order order, PayType payType);
    void ticketPay(Order order,String coupuns,String tickets);


    void taskUpdate(String taskId,String orderSn,Integer newOrderStatus,String candiUser,Boolean forceDispatch);//订单任务更新
    void taskModify(String taskId,String orderSn,String  userId);//订单重新指派

    void deleteById(Integer id);

    void cancelOrder(String orderSn);

    void OrderOperHistory(Order order,Integer orderStatus) ;

    void weixinPayOk(String orderSn,String weixinSn,Integer amount);

    List<GasCalcResult> caculate(String orderSn, String customerId, List<OrderCaculator> orderCaculators);

    void checkOverTime();

    void orderBindGasCynNumber(String orderSn,String gasCynNumbers);
}

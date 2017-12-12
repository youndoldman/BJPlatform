package com.donno.nj.service;


import com.donno.nj.domain.OrderOpHistory;

import java.util.List;
import java.util.Map;

public interface OrderOpHistoryService
{
    void create(OrderOpHistory orderOpHistory);

    List<OrderOpHistory> retrieve(Map params);

    Integer count(Map params);
}

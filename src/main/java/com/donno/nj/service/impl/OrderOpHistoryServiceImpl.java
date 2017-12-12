package com.donno.nj.service.impl;


import com.donno.nj.aspect.OperationLog;
import com.donno.nj.dao.OrderOpHistoryDao;
import com.donno.nj.domain.OrderOpHistory;
import com.donno.nj.service.OrderOpHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OrderOpHistoryServiceImpl implements OrderOpHistoryService
{
    @Autowired
    private OrderOpHistoryDao orderOpHistoryDao;

    @Override
    @OperationLog(desc = "创建订单更改记录信息")
    public void create(OrderOpHistory orderOpHistory)
    {
        orderOpHistoryDao.insert(orderOpHistory);
    }



    @Override
    @OperationLog(desc = "查询订单更改记录信息")
    public List<OrderOpHistory> retrieve(Map params)
    {
        List<OrderOpHistory> orderOpHistories = orderOpHistoryDao.getList(params);
        return orderOpHistories;
    }

    @Override
    @OperationLog(desc = "查询订单更改记录数量")
    public Integer count(Map params) {
        return orderOpHistoryDao.count(params);
    }
}

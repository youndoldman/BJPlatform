package com.donno.nj.service.impl;


import com.donno.nj.aspect.OperationLog;
import com.donno.nj.dao.OrderOpHistoryDao;
import com.donno.nj.domain.OrderOpHistory;
import com.donno.nj.service.OrderOpHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}

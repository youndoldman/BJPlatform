package com.donno.nj.service.impl;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.dao.*;
import com.donno.nj.domain.*;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.service.TicketOrderService;
import com.donno.nj.service.TicketService;
import com.donno.nj.util.Clock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class TicketOrderServiceImpl implements TicketOrderService
{

    @Autowired
    private TicketOrderDao ticketOrderDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    TicketDao ticketDao;

    @Override
    @OperationLog(desc = "查询气票消费信息")
    public List<TicketOrder> retrieve(Map params)
    {
        List<TicketOrder> ticketOrders = ticketOrderDao.getList(params);
        return ticketOrders;
    }

    @Override
    @OperationLog(desc = "查询气票消费信息")
    public Integer count(Map params) {
        return ticketOrderDao.count(params);
    }

    public void checkCustomer(String userId)
    {
        if (userId == null || userId.trim().length() == 0)
        {
            throw new ServerSideBusinessException("客户信息不全，请补充！", HttpStatus.NOT_ACCEPTABLE);
        }

        User target = userDao.findByUserId(userId);
        if (target == null)
        {
            throw new ServerSideBusinessException("客户不存在！", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    public void checkTicket(TicketOrder ticketOrder)
    {
        if (ticketOrder.getTicketIdx() == null )
        {
            throw new ServerSideBusinessException("气票信息不全，请补充！", HttpStatus.NOT_ACCEPTABLE);
        }

        Ticket target = ticketDao.findById(ticketOrder.getTicketIdx() );
        if (target == null)
        {
            throw new ServerSideBusinessException("气票信息不存在！", HttpStatus.NOT_ACCEPTABLE);
        }
    }


    public void checkOrder(String OrderSn)
    {
        if (OrderSn == null )
        {
            throw new ServerSideBusinessException("订单信息不全，请补充！", HttpStatus.NOT_ACCEPTABLE);
        }

        Order target = orderDao.findBySn(OrderSn);
        if (target == null)
        {
            throw new ServerSideBusinessException("订单信息不存在！", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @Override
    @OperationLog(desc = "添加气票消费信息")
    public void create(TicketOrder ticketOrder)
    {
        /*参数校验*/
        if (ticketOrder == null)
        {
            throw new ServerSideBusinessException("气票消费信息不全，请补充信息！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*客户信息校验*/
        checkCustomer(ticketOrder.getUserId());

        /*气票检查*/
        checkTicket(ticketOrder);

        /*订单检查*/
        checkOrder(ticketOrder.getOrderSn());

        ticketOrderDao.insert(ticketOrder);
    }



    @Override
    @OperationLog(desc = "删除气票消费信息")
    public void deleteById(Integer id)
    {
        TicketOrder ticketOrder = ticketOrderDao.findById(id);
        if (ticketOrder == null)
        {
            throw new ServerSideBusinessException("气票消费记录信息不存在！", HttpStatus.NOT_ACCEPTABLE);
        }

        ticketOrderDao.delete(id);
    }





}

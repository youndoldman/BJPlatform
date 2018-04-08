package com.donno.nj.service.impl;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.dao.*;
import com.donno.nj.domain.*;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.service.TicketService;
import com.donno.nj.util.Clock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Date;

@Service
public class TicketServiceImpl implements TicketService
{

    @Autowired
    private TicketDao ticketDao;

    @Autowired
    private UserDao userDao;

//    @Autowired
//    private GasCylinderSpecDao gasCylinderSpecDao;

    @Autowired
    private GoodsDao goodsDao;

    @Override
    @OperationLog(desc = "查询气票信息")
    public List<Ticket> retrieve(Map params)
    {
        List<Ticket> tickets = ticketDao.getList(params);
        return tickets;
    }

    @Override
    @OperationLog(desc = "查询气票熟量")
    public Integer count(Map params) {
        return ticketDao.count(params);
    }

    public void checkCustomer(User customer)
    {
        if (customer == null || customer.getUserId() == null || customer.getUserId().trim().length() == 0)
        {
            throw new ServerSideBusinessException("客户信息不全，请补充！", HttpStatus.NOT_ACCEPTABLE);
        }

        User target = userDao.findByUserId(customer.getUserId());
        if (target == null)
        {
            throw new ServerSideBusinessException("客户不存在！", HttpStatus.NOT_ACCEPTABLE);
        }

        customer.setId(target.getId());
    }

    public void checkOperator(User operator)
    {
        if (operator == null || operator.getUserId() == null || operator.getUserId().trim().length() == 0)
        {
            throw new ServerSideBusinessException("操作员信息不全，请补充！", HttpStatus.NOT_ACCEPTABLE);
        }

        User target = userDao.findByUserId(operator.getUserId());
        if (userDao.findByUserId(operator.getUserId()) == null)
        {
            throw new ServerSideBusinessException("操作员不存在！", HttpStatus.NOT_ACCEPTABLE);
        }
        operator.setId(target.getId());
    }


//    public void checkSpec(GasCylinderSpec spec)
//    {
//        if (spec == null || spec.getCode() == null || spec.getCode().trim().length() == 0)
//        {
//            throw new ServerSideBusinessException("缺少规格信息！", HttpStatus.NOT_ACCEPTABLE);
//        }
//
//        GasCylinderSpec target = gasCylinderSpecDao.findByCode(spec.getCode());
//        if (target == null)
//        {
//            throw new ServerSideBusinessException("规格信息错误！", HttpStatus.NOT_ACCEPTABLE);
//        }
//        spec.setId(target.getId());
//
//    }

    public void checkSpec(String specCode)
    {
        if (specCode == null || specCode.trim().length() == 0)
        {
            throw new ServerSideBusinessException("缺少规格信息！", HttpStatus.NOT_ACCEPTABLE);
        }

        Goods target = goodsDao.findByCode(specCode);
        if (target == null)
        {
            throw new ServerSideBusinessException("规格信息不存在！", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    public void checkStartDate(Date startDate)
    {
        if (startDate == null)
        {
            throw new ServerSideBusinessException("缺少生效起始日期！", HttpStatus.NOT_ACCEPTABLE);
        }

        if (!Clock.beforeDate(new Date(),startDate))
        {
            throw new ServerSideBusinessException("起始日期早于当前日期！", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    public void checkEndDate(Date endDate)
    {
        if (endDate == null)
        {
            throw new ServerSideBusinessException("缺少生效结束日期！", HttpStatus.NOT_ACCEPTABLE);
        }

        if (!Clock.beforeDate(new Date(),endDate))
        {
            throw new ServerSideBusinessException("结束日期早于当前日期！", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @Override
    @OperationLog(desc = "添加气票信息")
    public void create(Ticket ticket)
    {
        /*参数校验*/
        if (ticket == null)
        {
            throw new ServerSideBusinessException("气票信息不全，请补充信息！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*客户信息校验*/
        checkCustomer(ticket.getCustomer());

        /*操作员信息校验*/
        checkOperator(ticket.getOperator());

        /*规格检查*/
//        checkSpec(ticket.getSpec());
        checkSpec(ticket.getSpecCode());

        /*生效日期检查*/
        checkStartDate(ticket.getStartDate());
        checkEndDate(ticket.getEndDate());

        ticketDao.insert(ticket);
    }

    @Override
    @OperationLog(desc = "修改气票信息")
    public void update(Integer id, Ticket newTicket)
    {
        /*参数校验*/
        if (newTicket == null )
        {
            throw new ServerSideBusinessException("气票信息不能为空！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*气票是否存在*/
        Ticket ticket = ticketDao.findById(id);
        if (ticket == null)
        {
            throw new ServerSideBusinessException("气票不存在！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*已经使用的气票不允许修改*/
        if(ticket.getTicketStatus() == TicketStatus.TSUsed)
        {
            throw new ServerSideBusinessException("气票已经使用，不允许修改！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*客户信息校验*/
        if (newTicket.getCustomer() != null)
        {
            checkCustomer(newTicket.getCustomer());
        }

        /*操作员信息校验*/
        if (newTicket.getOperator() != null)
        {
            checkOperator(newTicket.getOperator());
        }

        /*规格检查*/
        if (newTicket.getSpecCode() != null)
        {
//            checkSpec(newTicket.getSpec());
            checkSpec(ticket.getSpecCode());
        }

        /*生效日期检查*/
        if (newTicket.getStartDate() != null)
        {
            checkStartDate(ticket.getStartDate());
        }
        if (newTicket.getStartDate() != null)
        {
            checkEndDate(ticket.getEndDate());
        }


        /*更新数据*/
        newTicket.setId(id);
        ticketDao.update(newTicket);
    }

    @Override
    @OperationLog(desc = "删除气票信息")
    public void deleteById(Integer id)
    {
        Ticket ticket = ticketDao.findById(id);
        if (ticket == null)
        {
            throw new ServerSideBusinessException("气票信息不存在！", HttpStatus.NOT_ACCEPTABLE);
        }

         /*已经使用的气票不允许修改*/
        if(ticket.getTicketStatus() == TicketStatus.TSUsed)
        {
            throw new ServerSideBusinessException("气票已经使用，不允许删除！", HttpStatus.NOT_ACCEPTABLE);
        }

        ticketDao.delete(id);
    }





}

package com.donno.nj.service.impl;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.dao.*;
import com.donno.nj.domain.*;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.service.TicketService;
import com.donno.nj.util.Clock;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class TicketServiceImpl implements TicketService
{
    @Autowired
    private TicketDao ticketDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private GoodsDao goodsDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Override
    @OperationLog(desc = "查询气票信息")
    public List<Ticket> retrieve(Map params)
    {
        List<Ticket> tickets = new ArrayList<Ticket>();

        if (params.containsKey("departmentCode"))
        {
            recurseRetreve(params,tickets);
        }
        else
        {
            tickets = ticketDao.getList(params);
        }

        return tickets;
    }

    /*子公司递归统计*/
    public void recurseRetreve(Map params, List<Ticket> ticketList)
    {
        String departmentCode = params.get("departmentCode").toString();
        Department department = departmentDao.findByCode(departmentCode);
        if(department == null)
        {
            throw new ServerSideBusinessException("部门信息不存在！", HttpStatus.NOT_ACCEPTABLE);
        }
        department.setLstSubDepartment(departmentDao.findSubDep(department.getId()));
        if ( department.getLstSubDepartment() != null && department.getLstSubDepartment().size() > 0 )
        {
            for (Department childDep : department.getLstSubDepartment())
            {
                Map subParam = new HashMap<String,String>();
                subParam.putAll(params);
                subParam.remove("departmentCode");
                subParam.putAll(ImmutableMap.of("departmentCode", childDep.getCode()));

                List<Ticket> subTicket  = new ArrayList<Ticket>();
                recurseRetreve(subParam,subTicket);

                mergeTicket(subTicket,ticketList);
            }
        }
        else
        {
            ticketList.addAll(ticketDao.getList(params));
        }
    }


    public void mergeTicket(List<Ticket> tickets,List<Ticket> targets)
    {
        for (Ticket ticket :tickets)
        {
            boolean finded = false;
            for (Ticket target :targets)
            {
                if (!ticket.getTicketSn().equals(target.getTicketSn()))
                {
                   //
                }
                else
                {
                    finded = true;
                    break;
                }
            }

            if (!finded)
            {
                targets.add(ticket);
            }
        }
    }


    @Override
    @OperationLog(desc = "查询气票数量")
    public Integer count(Map params)
    {
        Integer ticketCount = 0 ;

        if (params.containsKey("departmentCode"))
        {
            ticketCount = recurseCount(params);
        }
        else
        {
            ticketCount = ticketDao.count(params);
        }

        return ticketCount;
    }

    Integer recurseCount(Map params)
    {
        Integer ticketCount = 0;
        String departmentCode = params.get("departmentCode").toString();
        Department department = departmentDao.findByCode(departmentCode);
        if(department == null)
        {
            throw new ServerSideBusinessException("部门信息不存在！", HttpStatus.NOT_ACCEPTABLE);
        }
        department.setLstSubDepartment(departmentDao.findSubDep(department.getId()));
        if ( department.getLstSubDepartment() != null && department.getLstSubDepartment().size() > 0 )
        {
            for (Department childDep : department.getLstSubDepartment())
            {
                Map subParam = new HashMap<String,String>();
                subParam.putAll(params);
                subParam.remove("departmentCode");
                subParam.putAll(ImmutableMap.of("departmentCode", childDep.getCode()));

                Integer subTicketCount = recurseCount(subParam);

                ticketCount = ticketCount + subTicketCount;
            }
        }
        else
        {
            ticketCount = ticketDao.count(params);
        }

        return ticketCount;
    }



    public void checkTicketSn(String ticketSn)
    {
        if (ticketSn == null || ticketSn.trim().length() == 0)
        {
            throw new ServerSideBusinessException("缺少气票编号！", HttpStatus.NOT_ACCEPTABLE);
        }

        Ticket ticket = ticketDao.findBySn(ticketSn);
        if (ticket != null)
        {
            String message = String.format("气票编号%s已经存在",ticketSn);
            throw new ServerSideBusinessException(message, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    public void checkCustomer(User customer)
    {
        if (customer == null || customer.getUserId() == null || customer.getUserId().trim().length() == 0)
        {
            throw new ServerSideBusinessException("客户信息不全，请补充！", HttpStatus.NOT_ACCEPTABLE);
        }


        /*只有气票用户才能增加气票*/
        Customer target = customerDao.findByCstUserId(customer.getUserId());
        if (target == null)
        {
            throw new ServerSideBusinessException("客户不存在！", HttpStatus.NOT_ACCEPTABLE);
        }
        if (!target.getSettlementType().getCode().equals(ServerConstantValue.SETTLEMENT_TYPE_TICKET))
        {
            throw new ServerSideBusinessException("客户不是气票用户，不能使用气票！", HttpStatus.NOT_ACCEPTABLE);
        }

        customer.setId(target.getId());
    }

    public void checkSaleman(String salemanId)
    {
        if (salemanId == null || salemanId.trim().length() == 0)
        {
            throw new ServerSideBusinessException("业务员信息不全，请补充！", HttpStatus.NOT_ACCEPTABLE);
        }

        User target = userDao.findByUserId(salemanId);
        if (target == null)
        {
            throw new ServerSideBusinessException("业务员不存在！", HttpStatus.NOT_ACCEPTABLE);
        }
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
    public void create(Ticket ticket,Integer ticketCount)
    {
        /*参数校验*/
        if (ticket == null)
        {
            throw new ServerSideBusinessException("气票信息不全，请补充信息！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*客户信息校验*/
        checkCustomer(ticket.getCustomer());

        /*业务员信息校验*/
        checkSaleman(ticket.getSalemanId());

        /*规格检查*/
        String specCode = ticket.getSpecCode();
        if (specCode == null || specCode.trim().length() == 0)
        {
            throw new ServerSideBusinessException("缺少规格信息！", HttpStatus.NOT_ACCEPTABLE);
        }

        Goods targetGoods = goodsDao.findByCode(specCode);
        if (targetGoods == null)
        {
            throw new ServerSideBusinessException("规格信息不存在！", HttpStatus.NOT_ACCEPTABLE);
        }

        //设置价格
        //ticket.setPrice(targetGoods.getPrice());

        /*生效日期检查*/
        checkStartDate(ticket.getStartDate());
        checkEndDate(ticket.getEndDate());

        String ticketSn = "TSN-";
        Date curDate = new Date();
        String dateFmt =  new SimpleDateFormat("yyyyMMddHHmmssSSS").format(curDate);
        ticketSn = ticketSn + (dateFmt);

        for (Integer iCount = 0 ;iCount < ticketCount ; iCount++)
        {
            Ticket newTicket = new Ticket();
            newTicket = ticket;

            //生成气票编号
            String newSn = String.format("%s-%d",ticketSn,iCount+1);
            newTicket.setTicketSn(newSn);

            ticketDao.insert(newTicket);
        }
    }

    @Override
    @OperationLog(desc = "修改气票信息")
    public void update(String ticketSn, Ticket newTicket)
    {
        /*参数校验*/
        if (newTicket == null )
        {
            throw new ServerSideBusinessException("气票信息不能为空！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*气票是否存在*/
        Ticket ticket = ticketDao.findBySn(ticketSn);
        if (ticket == null)
        {
            throw new ServerSideBusinessException("气票不存在！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*已经使用的气票不允许修改*/
//        if(ticket.getTicketStatus() == TicketStatus.TSUsed)
//        {
//            throw new ServerSideBusinessException("气票已经使用，不允许修改！", HttpStatus.NOT_ACCEPTABLE);
//        }

        /*客户信息校验*/
        if (newTicket.getCustomer() != null)
        {
            checkCustomer(newTicket.getCustomer());
        }

        /*操作员信息校验*/
        if (newTicket.getSalemanId() != null)
        {
            checkSaleman(newTicket.getSalemanId());
        }

        /*规格检查*/
        if (newTicket.getSpecCode() != null)
        {
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
        newTicket.setId(ticket.getId());
        ticketDao.update(newTicket);
    }

    @Override
    @OperationLog(desc = "删除气票信息")
    public void deleteBySn(String ticketSn)
    {
        Ticket ticket = ticketDao.findBySn(ticketSn);
        if (ticket == null)
        {
            throw new ServerSideBusinessException("气票信息不存在！", HttpStatus.NOT_ACCEPTABLE);
        }

         /*已经使用的气票不允许修改*/
        if(ticket.getTicketStatus() == TicketStatus.TSUsed)
        {
            throw new ServerSideBusinessException("气票已经使用，不允许删除！", HttpStatus.NOT_ACCEPTABLE);
        }

        ticketDao.delete(ticket.getId());
    }

    @Override
    @OperationLog(desc = "删除气票信息")
    public void deleteById(Integer id)
    {
        Ticket ticket = ticketDao.findById(id);
        if (ticket == null)
        {
            throw new ServerSideBusinessException("气票信息不存在！", HttpStatus.NOT_FOUND);
        }

         /*已经使用的气票不允许修改*/
        if(ticket.getTicketStatus() == TicketStatus.TSUsed)
        {
            throw new ServerSideBusinessException("气票已经使用，不允许删除！", HttpStatus.NOT_ACCEPTABLE);
        }

        ticketDao.delete(id);
    }





}

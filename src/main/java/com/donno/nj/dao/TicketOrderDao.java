package com.donno.nj.dao;


import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.TicketOrder;

public interface TicketOrderDao extends BaseDao<TicketOrder>
{
    TicketOrder findByTicketIdx(Integer ticketIdx);
}

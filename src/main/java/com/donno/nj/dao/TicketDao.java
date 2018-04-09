package com.donno.nj.dao;


import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.AdjustPriceHistory;
import com.donno.nj.domain.Goods;
import com.donno.nj.domain.Ticket;

import java.util.List;
import java.util.Map;

public interface TicketDao extends BaseDao<Ticket>
{
    Ticket findBySn(String ticketSn);
}

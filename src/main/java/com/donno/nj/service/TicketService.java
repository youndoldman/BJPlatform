package com.donno.nj.service;

import com.donno.nj.domain.AdjustPriceHistory;
import com.donno.nj.domain.Goods;
import com.donno.nj.domain.Ticket;
import com.google.common.base.Optional;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
public interface TicketService
{
    List<Ticket> retrieve(Map params);

    Integer count(Map params);

    void create(Ticket ticket);

    void update(Integer id, Ticket newTicket);

    void deleteById(Integer id);


}
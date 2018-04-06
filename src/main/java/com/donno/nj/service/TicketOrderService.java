package com.donno.nj.service;

import com.donno.nj.domain.Ticket;
import com.donno.nj.domain.TicketOrder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
public interface TicketOrderService
{
    List<TicketOrder> retrieve(Map params);

    Integer count(Map params);

    void create(TicketOrder ticket);


    void deleteById(Integer id);

}

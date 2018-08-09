package com.donno.nj.controller;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.constant.Constant;
import com.donno.nj.domain.SettlementType;
import com.donno.nj.domain.Ticket;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.representation.ListRep;
import com.donno.nj.service.SettlementTypeService;
import com.donno.nj.service.TicketService;
import com.donno.nj.service.impl.TicketServiceImpl;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.donno.nj.util.ParamMapBuilder.paginationParams;

@RestController
public class TicketController
{
    @Autowired
    TicketService ticketService ;


    @RequestMapping(value = "/api/Ticket", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "获取气票信息列表")
    public ResponseEntity retrieve(@RequestParam(value = "ticketSn", defaultValue = "") String ticketSn,
                                   @RequestParam(value = "customerUserId", defaultValue = "") String customerUserId,
                                   @RequestParam(value = "operatorUserId", defaultValue = "") String operatorUserId,
                                   @RequestParam(value = "specCode", defaultValue = "") String specCode,
                                   @RequestParam(value = "useStatus", required = false) Integer useStatus,
                                   @RequestParam(value = "orderBy", defaultValue = "") String orderBy,
                                   @RequestParam(value = "pageSize", defaultValue = Constant.PAGE_SIZE) Integer pageSize,
                                   @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo)
    {
        Map params = new HashMap<String,String>();

        if (ticketSn.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("ticketSn", ticketSn));
        }

        if (customerUserId.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("customerUserId", customerUserId));
        }

        if (operatorUserId.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("operatorUserId", operatorUserId));
        }

        if (specCode.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("specCode", specCode));
        }

        if (useStatus != null)
        {
            params.putAll(ImmutableMap.of("useStatus", useStatus));
        }

        params.putAll(paginationParams(pageNo, pageSize, orderBy));

        List<Ticket> tickets = ticketService.retrieve(params);
        Integer count = ticketService.count(params);

        return ResponseEntity.ok(ListRep.assemble(tickets, count));
    }


    @OperationLog(desc = "增加气票信息")
    @RequestMapping(value = "/api/Ticket", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody Ticket ticket, UriComponentsBuilder ucBuilder)
    {
        ResponseEntity responseEntity;

        /*创建*/
        ticketService.create(ticket);

        URI uri = ucBuilder.path("/api/Ticket/{id}").buildAndExpand(ticket.getId()).toUri();
        responseEntity = ResponseEntity.created(uri).build();

        return responseEntity;
    }


    @OperationLog(desc = "修改气票信息")
    @RequestMapping(value = "/api/Ticket/{ticketSn}", method = RequestMethod.PUT)
    public ResponseEntity update(@PathVariable("ticketSn") String ticketSn,
                                 @RequestBody Ticket newTicket)
    {
        ResponseEntity responseEntity;

        ticketService.update(ticketSn, newTicket);

        responseEntity = ResponseEntity.ok().build();

        return responseEntity;
    }



    @OperationLog(desc = "删除气票信息")
    @RequestMapping(value = "/api/Ticket/{ticketSn}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable("ticketSn") String ticketSn)
    {
        ResponseEntity responseEntity;

        ticketService.deleteBySn(ticketSn);

        responseEntity = ResponseEntity.noContent().build();

        return responseEntity;
    }
}

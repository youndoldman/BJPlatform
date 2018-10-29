package com.donno.nj.controller;

import com.donno.nj.aspect.Auth;
import com.donno.nj.aspect.OperationLog;
import com.donno.nj.constant.Constant;
import com.donno.nj.domain.Advice;
import com.donno.nj.domain.OrderUrgency;
import com.donno.nj.domain.ServerConstantValue;
import com.donno.nj.representation.ListRep;
import com.donno.nj.service.AdviceService;
import com.donno.nj.service.OrderUrgencyService;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.donno.nj.util.ParamMapBuilder.paginationParams;

@RestController
public class OrderUrgencyController
{
    @Autowired
    OrderUrgencyService orderUrgencyService ;

    @RequestMapping(value = "/api/OrderUrgency", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "获取订单催气信息列表")
    public ResponseEntity retrieve(@RequestParam(value = "orderSn", defaultValue = "") String orderSn,
                                   @RequestParam(value = "userID", defaultValue = "") String userId,
                                   @RequestParam(value = "startTime", defaultValue = "") String startTime,
                                   @RequestParam(value = "endTime", defaultValue = "") String endTime,
                                   @RequestParam(value = "orderBy", defaultValue = "") String orderBy,
                                   @RequestParam(value = "pageSize", defaultValue = Constant.PAGE_SIZE) Integer pageSize,
                                   @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo)
    {
        Map params = new HashMap<String,String>();

        if (orderSn.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("orderSn", orderSn));
        }

        if (userId.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("userId", userId));
        }

        if (startTime.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("startTime", startTime));
        }

        if (endTime.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("endTime", endTime));
        }

        params.putAll(paginationParams(pageNo, pageSize, orderBy));

        List<OrderUrgency> orderUrgencies = orderUrgencyService.retrieve(params);
        Integer count = orderUrgencyService.count(params);

        return ResponseEntity.ok(ListRep.assemble(orderUrgencies, count));
    }


    @OperationLog(desc = "增加催单信息")
    @RequestMapping(value = "/api/OrderUrgency", method = RequestMethod.POST)
    //@Auth(allowedBizOp = {ServerConstantValue.GP_ADMIN,ServerConstantValue.GP_CUSTOMER_SERVICE })
    public ResponseEntity create(@RequestBody OrderUrgency orderUrgency,
                                 UriComponentsBuilder ucBuilder)
    {
        ResponseEntity responseEntity;

        /*创建*/
        orderUrgencyService.create(orderUrgency);

        URI uri = ucBuilder.path("/api/OrderUrgency/{id}").buildAndExpand(orderUrgency.getId()).toUri();
        responseEntity = ResponseEntity.created(uri).build();

        return responseEntity;
    }


    @OperationLog(desc = "删除催单信息")
    @RequestMapping(value = "/api/OrderUrgency/{id}", method = RequestMethod.DELETE)
    //@Auth(allowedBizOp = {ServerConstantValue.GP_ADMIN,ServerConstantValue.GP_CUSTOMER_SERVICE })
    public ResponseEntity delete(@PathVariable("id") Integer id)
    {
        ResponseEntity responseEntity;

        orderUrgencyService.deleteById(id);

        responseEntity = ResponseEntity.noContent().build();

        return responseEntity;
    }
}

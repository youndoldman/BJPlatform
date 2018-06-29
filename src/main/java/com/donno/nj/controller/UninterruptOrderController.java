package com.donno.nj.controller;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.constant.Constant;
import com.donno.nj.domain.*;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.representation.ListRep;
import com.donno.nj.service.*;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.donno.nj.util.ParamMapBuilder.paginationParams;

@RestController
public class UninterruptOrderController
{
    @Autowired
    private UninterruptGasOrderService uninterruptGasOrderService;


    @Autowired
    private SysUserService sysUserService;

    @RequestMapping(value = "/api/UninterruptOrders", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "获取不间断供气订单列表")
    public ResponseEntity retrieve(@RequestParam(value = "orderSn", defaultValue = "") String orderSn,
                                   @RequestParam(value = "userId", defaultValue = "") String userId,
                                   @RequestParam(value = "payStatus", required = false) Integer payStatus,
                                   @RequestParam(value = "payType", required = false) Integer payType,
                                   @RequestParam(value = "gasCynNumber", defaultValue = "") String gasCynNumber,
                                   @RequestParam(value = "dispatchOrderSn", defaultValue = "") String dispatchOrderSn,
                                   @RequestParam(value = "dispatcherId", defaultValue = "") String dispatcherId,
                                   @RequestParam(value = "startTime", defaultValue = "") String startTime,
                                   @RequestParam(value = "endTime", defaultValue = "") String endTime,
                                   @RequestParam(value = "departmentCode", defaultValue = "") String departmentCode,
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

        if (dispatchOrderSn.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("dispatchOrderSn", dispatchOrderSn));
        }

        if (payStatus != null)
        {
            params.putAll(ImmutableMap.of("payStatus", payStatus));
        }

        if (payType != null)
        {
            params.putAll(ImmutableMap.of("payType", payType));
        }

        if ( startTime != null && startTime.trim().length() > 0 )
        {
            params.putAll(ImmutableMap.of("startTime", startTime));
        }

        if ( endTime != null && endTime.trim().length() > 0 )
        {
            params.putAll(ImmutableMap.of("endTime", endTime));
        }

        if ( dispatcherId != null && dispatcherId.trim().length() > 0 )
        {
            params.putAll(ImmutableMap.of("dispatcherId", dispatcherId));
        }

        if ( gasCynNumber != null && gasCynNumber.trim().length() > 0 )
        {
            params.putAll(ImmutableMap.of("gasCynNumber", gasCynNumber));
        }

        if (departmentCode.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("departmentCode", departmentCode));
        }

        params.putAll(paginationParams(pageNo, pageSize, orderBy));

        List<UninterruptedGasOrder> orders = uninterruptGasOrderService.retrieve(params);
        Integer count = uninterruptGasOrderService.count(params);
        return ResponseEntity.ok(ListRep.assemble(orders, count));
    }

    @OperationLog(desc = "创建不间断供气订单")
    @RequestMapping(value = "/api/UninterruptOrders", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody UninterruptedGasOrder order, UriComponentsBuilder ucBuilder)
    {
        ResponseEntity responseEntity;

        /*创建订单入库*/
        uninterruptGasOrderService.create(order);

        URI uri = ucBuilder.path("/api/UninterruptOrders/{orderSn}").buildAndExpand(order.getOrderSn()).toUri();
        responseEntity = ResponseEntity.created(uri).build();

        return responseEntity;
    }

    @OperationLog(desc = "支付金额计算")
    @RequestMapping(value = "/api/UninterruptOrders/Caculate/{orderSn}", method = RequestMethod.GET)
    public ResponseEntity GasPayCaculate(@PathVariable("orderSn") String orderSn,
                                         @RequestParam(value = "emptyWeight", defaultValue = "") Float emptyWeight
                                        )
    {
        return ResponseEntity.ok(uninterruptGasOrderService.GasPayCaculate(orderSn,emptyWeight)) ;
    }

    @OperationLog(desc = "支付")
    @RequestMapping(value = "/api/UninterruptOrders/Pay/{orderSn}", method = RequestMethod.GET)
    public ResponseEntity ticketPay(@PathVariable("orderSn") String orderSn,
                                    @RequestParam(value = "payType", required = true) Integer payType,
                                    @RequestParam(value = "emptyWeight", required = true) Float emptyWeight
                                    )
    {
        ResponseEntity responseEntity;

        Optional<UninterruptedGasOrder> orderOptional = uninterruptGasOrderService.findBySn(orderSn);
        if (orderOptional.isPresent())
        {
            if((payType < PayType.PTOnLine.getIndex()) || (payType > PayType.PTCheck.getIndex() ))
            {
                throw new ServerSideBusinessException("支付类型错误！", HttpStatus.NOT_ACCEPTABLE);
            }

            uninterruptGasOrderService.GasPay(orderSn,PayType.values()[payType],emptyWeight);
            responseEntity = ResponseEntity.ok().build();
        }
        else
        {
            responseEntity = ResponseEntity.notFound().build();
        }

        return responseEntity;
    }

    @OperationLog(desc = "删除订单信息")
    @RequestMapping(value = "/api/UninterruptOrders/{orderSn}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable("orderSn") String orderSn)
    {
        ResponseEntity responseEntity;

        Optional<UninterruptedGasOrder> orderOptional = uninterruptGasOrderService.findBySn(orderSn);
        if (orderOptional.isPresent())
        {
            uninterruptGasOrderService.deleteById(orderOptional.get().getId());
            responseEntity = ResponseEntity.noContent().build();
        }
        else
        {
            responseEntity = ResponseEntity.notFound().build();
        }

        return responseEntity;
    }


    @OperationLog(desc = "订单作废")
    @RequestMapping(value = "/api/CancelUninterruptOrders/{orderSn}", method = RequestMethod.PUT)
    public ResponseEntity cancelOrder(@PathVariable("orderSn") String orderSn)
    {
        ResponseEntity responseEntity;

        Optional<UninterruptedGasOrder> orderOptional = uninterruptGasOrderService.findBySn(orderSn);
        if (orderOptional.isPresent())
        {

            responseEntity = ResponseEntity.noContent().build();
        }
        else
        {
            responseEntity = ResponseEntity.notFound().build();
        }

        return responseEntity;
    }



}

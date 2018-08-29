package com.donno.nj.controller;

import com.donno.nj.aspect.Auth;
import com.donno.nj.aspect.OperationLog;
import com.donno.nj.constant.Constant;
import com.donno.nj.domain.CouponOrder;
import com.donno.nj.domain.ServerConstantValue;
import com.donno.nj.domain.TicketOrder;
import com.donno.nj.representation.ListRep;
import com.donno.nj.service.CouponOrderService;
import com.donno.nj.service.TicketOrderService;
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
public class CouponOrderController
{

    @Autowired
    CouponOrderService couponOrderService ;

    @RequestMapping(value = "/api/CouponOrder", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "获取优惠券消费信息列表")
    //@Auth(allowedBizOp = {})
    public ResponseEntity retrieve(@RequestParam(value = "userId", defaultValue = "") String userId,
                                   @RequestParam(value = "orderSn", defaultValue = "") String orderSn,
                                   @RequestParam(value = "useTimeStart", defaultValue = "") String useTimeStart,
                                   @RequestParam(value = "useTimeEnd", defaultValue = "") String useTimeEnd,
                                   @RequestParam(value = "orderBy", defaultValue = "") String orderBy,
                                   @RequestParam(value = "pageSize", defaultValue = Constant.PAGE_SIZE) Integer pageSize,
                                   @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo)
    {
        Map params = new HashMap<String,String>();

        if (userId.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("userId", userId));
        }

        if (orderSn.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("orderSn", orderSn));
        }

        if (useTimeStart.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("useTimeStart", useTimeStart));
        }

        if (useTimeEnd.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("useTimeEnd", useTimeEnd));
        }

        params.putAll(paginationParams(pageNo, pageSize, orderBy));

        List<CouponOrder> couponOrders = couponOrderService.retrieve(params);
        Integer count = couponOrderService.count(params);

        return ResponseEntity.ok(ListRep.assemble(couponOrders, count));
    }


    @OperationLog(desc = "增加优惠券消费信息")
    @RequestMapping(value = "/api/CouponOrder", method = RequestMethod.POST)
    //@Auth(allowedBizOp = {ServerConstantValue.GP_ADMIN ,ServerConstantValue.GP_CUSTOMER_SERVICE })
    public ResponseEntity create(@RequestBody CouponOrder couponOrder, UriComponentsBuilder ucBuilder)
    {
        ResponseEntity responseEntity;

        /*创建*/
        couponOrderService.create(couponOrder);

        URI uri = ucBuilder.path("/api/CouponOrder/{id}").buildAndExpand(couponOrder.getId()).toUri();
        responseEntity = ResponseEntity.created(uri).build();

        return responseEntity;
    }




    @OperationLog(desc = "删除优惠消费信息")
    @RequestMapping(value = "/api/CouponOrder/{id}", method = RequestMethod.DELETE)
    @Auth(allowedBizOp = {ServerConstantValue.GP_ADMIN ,ServerConstantValue.GP_CUSTOMER_SERVICE })
    public ResponseEntity delete(@PathVariable("id") Integer id)
    {
        ResponseEntity responseEntity;

        couponOrderService.delete(id);

        responseEntity = ResponseEntity.noContent().build();

        return responseEntity;
    }
}

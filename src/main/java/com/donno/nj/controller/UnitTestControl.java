package com.donno.nj.controller;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UnitTestControl
{
    @Autowired
    private OrderService orderService;

    @OperationLog(desc = "绑定微信定单号")
    @RequestMapping(value = "/api/Orders/weixin/{orderSn}", method = RequestMethod.POST)
    public ResponseEntity weixinPayOk(@PathVariable("orderSn") String orderSn,
                                      @RequestParam(value = "weixinOrderSn", defaultValue = "") String weixinOrderSn,
                                      @RequestParam(value = "amount", required = true ) Integer amount)
    {
        ResponseEntity responseEntity;

        /*创建订单入库*/
        orderService.weixinPayOk(orderSn,weixinOrderSn,amount);

        responseEntity = ResponseEntity.ok().build();

        return responseEntity;
    }
}

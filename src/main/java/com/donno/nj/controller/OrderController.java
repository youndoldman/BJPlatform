package com.donno.nj.controller;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.constant.Constant;
import com.donno.nj.domain.Order;
import com.donno.nj.domain.Customer;
import com.donno.nj.domain.User;
import com.donno.nj.domain.OrderDetail;
import com.donno.nj.representation.ListRep;
import com.donno.nj.service.CustomerService;
import com.donno.nj.service.OrderService;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static com.donno.nj.util.ParamMapBuilder.paginationParams;
import static com.google.common.collect.Maps.newHashMap;

@RestController
public class OrderController
{
    @Autowired
    private OrderService orderService;

    @Autowired
    private CustomerService customerService;

    @RequestMapping(value = "/api/orders", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "获取订单列表")
    public ResponseEntity retrieve(@RequestParam(value = "", defaultValue = "") String customerId,
                                            @RequestParam(value = "orderBy", defaultValue = "") String orderBy,
                                            @RequestParam(value = "pageSize", defaultValue = Constant.PAGE_SIZE) Integer pageSize,
                                            @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo)
    {
        Map params = newHashMap(ImmutableMap.of("customerId", customerId));
        params.putAll(paginationParams(pageNo, pageSize, orderBy));

        List<Order> orders = orderService.retrieve(params);
        return ResponseEntity.ok(ListRep.assemble(orders, orders.size()));
    }

    @OperationLog(desc = "创建订单")
    @RequestMapping(value = "/api/orders", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody Order order, UriComponentsBuilder ucBuilder)
    {
        ResponseEntity responseEntity;

        /*校验客户信息*/
        if(order.getCustomerId() != null)
        {
            User customer = customerService.findByUserId(order.getCustomerId()).get();
            if ( customer != null)
            {
                order.setCustomerIdx (customer.getId());

                /*校验商品信息*/
                if(order.getOrderDetailList().size() > 0)
                {
                    orderService.create(order);
                    URI uri = ucBuilder.path("/api/orders/{id}").buildAndExpand(order.getId()).toUri();
                    responseEntity = ResponseEntity.created(uri).build();
                }
                else
                {
                    responseEntity =  ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();//没有商品信息
                }
            }
            else
            {
                responseEntity =  ResponseEntity.status(HttpStatus.NOT_FOUND).build();//没有客户信息
            }
        }
        else
        {
            responseEntity =  ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }

        return responseEntity;
    }

//    @OperationLog(desc = "修改商品信息")
//    @RequestMapping(value = "/api/goods/{name}", method = RequestMethod.PUT)
//    public ResponseEntity update(@PathVariable("name") String name, @RequestBody Goods newGoods)
//    {
//        ResponseEntity responseEntity;
//
//        Optional<Goods> goods = goodsService.findByName(name);
//        if (goods.isPresent())
//        {
//            /*产品类型信息校验*/
//            if (newGoods.getGoodsType() != null)
//            {
//                Optional<GoodsType> goodsType =  goodsTypeService.findByCode(newGoods.getGoodsType().getCode()) ;
//                if(!goodsType.isPresent())
//                {
//                    responseEntity =  ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
//                }
//                else
//                {
//                    newGoods.setGoodsType(goodsType.get());
//                    goodsService.update(goods.get().getId(),newGoods);
//                    responseEntity = ResponseEntity.ok().build();
//                }
//            }
//            else
//            {
//                responseEntity =  ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
//            }
//        }
//        else
//        {
//            responseEntity = ResponseEntity.notFound().build();
//        }
//
//        return responseEntity;
//    }
//
//    @OperationLog(desc = "删除商品信息")
//    @RequestMapping(value = "/api/goods/{name}", method = RequestMethod.DELETE)
//    public ResponseEntity delete(@PathVariable("name") String name)
//    {
//        ResponseEntity responseEntity;
//
//        Optional<Goods> goods = goodsService.findByName(name);
//        if (goods.isPresent())
//        {
//            goodsService.deleteById(goods.get().getId());
//            responseEntity = ResponseEntity.noContent().build();
//        }
//        else
//        {
//            responseEntity = ResponseEntity.notFound().build();
//        }
//
//        return responseEntity;
//    }
}

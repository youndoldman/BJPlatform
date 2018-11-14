package com.donno.nj.controller;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.constant.Constant;
import com.donno.nj.domain.*;

import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.representation.ListRep;
import com.donno.nj.service.*;
import com.donno.nj.util.AppUtil;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;

import org.apache.commons.lang3.tuple.Pair;
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
public class OrderController
{
    @Autowired
    private OrderService orderService;

    @Autowired
    private WorkFlowService workFlowService;

    @Autowired
    private OrderOpHistoryService orderOpHistoryService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private SysUserService sysUserService;


    @RequestMapping(value = "/api/TaskOrders/{userId}", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "获取任务订单列表")
    public ResponseEntity retrieveTaskOrder(@PathVariable("userId") String userId,
                                            @RequestParam(value = "orderStatus", defaultValue = "") Integer orderStatus,
                                            @RequestParam(value = "orderBy", defaultValue = "") String orderBy,
                                            @RequestParam(value = "pageSize", defaultValue = Constant.PAGE_SIZE) Integer pageSize,
                                            @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo)
    {

        int totalTaskCount = workFlowService.getTasksCountByUserId(userId,orderStatus);
        List<Task> taskList = workFlowService.getTasksByUserId(userId,orderStatus,pageNo,pageSize);

        List<Task> taskListNoOrder = new ArrayList<Task>();
        for (Task task:taskList)
        {
            Optional<Order> order = orderService.findBySn(task.getProcess().getBuinessKey());
            if (order.isPresent())
            {
                if(orderStatus != null )
                {
                    if (order.get().getOrderStatus() == orderStatus)
                    {
                        task.setObject(order.get());
                    }
                    else
                    {
                        taskListNoOrder.add(task);
                    }
                }
                else
                {
                    task.setObject(order.get());
                }
            }
        }

        /*移除不符合查询条件订单的任务*/
        for (Task task:taskListNoOrder)
        {
            taskList.remove(task);
        }

        return ResponseEntity.ok(ListRep.assemble(taskList, totalTaskCount));
    }


    @RequestMapping(value = "/api/TaskOrders/Modify/{taskId}", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "任务订单修改,订单转派等")
    public ResponseEntity taskOrderModify( @PathVariable("taskId") String taskId,
                                            @RequestParam(value = "businessKey", required = true) String businessKey,
                                            @RequestParam(value = "candiUser", required = true) String candiUser)
    {
        orderService.taskModify(taskId,businessKey,candiUser);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/api/TaskOrders/Process/{taskId}", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "任务订单处理")
    public ResponseEntity taskOrderProcess( @PathVariable("taskId") String taskId,
                                            @RequestParam(value = "businessKey", required = true) String businessKey,
                                            @RequestParam(value = "candiUser", required = true) String candiUser,
                                            @RequestParam(value = "orderStatus", required = true) Integer orderStatus,
                                            @RequestParam(value = "forceDispatch", required = false) Boolean forceDispatch )
    {
        /*orderStatus校验*/
        if(OrderStatus.getName(orderStatus) == null)
        {
            throw new ServerSideBusinessException("定单状态错误！", HttpStatus.NOT_ACCEPTABLE);
        }

        if (forceDispatch == null)
        {
            forceDispatch = false;
        }

        orderService.taskUpdate(taskId,businessKey,orderStatus,candiUser,forceDispatch);

        return ResponseEntity.ok().build();
    }

//    @RequestMapping(value = "/api/TaskOrders/Process/{taskId}", method = RequestMethod.GET, produces = "application/json")
//    @OperationLog(desc = "任务订单处理")
//    public ResponseEntity taskOrderProcess( @PathVariable("taskId") String taskId,
//                                            @RequestParam(value = "businessKey", required = true) String businessKey,
//                                            @RequestParam(value = "candiUser", required = true) String candiUser,
//                                            @RequestParam(value = "orderStatus", required = true) Integer orderStatus,
//                                            @RequestParam(value = "forceDispatch", required = false) Boolean forceDispatch )
//    {
//        /*orderStatus校验*/
//        if(OrderStatus.getName(orderStatus) == null)
//        {
//            throw new ServerSideBusinessException("定单不存在！", HttpStatus.NOT_ACCEPTABLE);
//        }
//
//        /*检查订单是否存在，更新订单状态*/
//        Optional<Order> orderOptional=  orderService.findBySn(businessKey);
//        if (!orderOptional.isPresent())
//        {
//            throw new ServerSideBusinessException("定单不存在！", HttpStatus.NOT_ACCEPTABLE);
//        }
//        else
//        {
//            //orderOptional.get().setOrderStatus(orderStatus);
//
//            Map<String, Object> variables = new HashMap<String, Object>();
//
//            Optional<Group>  group = groupService.findByCode(ServerConstantValue.GP_CUSTOMER_SERVICE);
//            if(group == null)
//            {
//                throw new ServerSideBusinessException("用户组信息错误！", HttpStatus.NOT_ACCEPTABLE);
//            }
//
//            if (orderStatus == OrderStatus.OSDispatching.getIndex())//派送
//            {
//                if(candiUser == null || candiUser.trim().length() == 0)
//                {
//                    throw new ServerSideBusinessException("用户不存在！", HttpStatus.NOT_ACCEPTABLE);
//                }
//
//                variables.put(ServerConstantValue.ACT_FW_STG_2_CANDI_USERS,candiUser);
//                variables.put(ServerConstantValue.ACT_FW_STG_2_CANDI_GROUPS,String.valueOf(group.get().getId()));
//            }
//            else if (orderStatus == OrderStatus.OSSigned.getIndex())//签收
//            {
//                if(candiUser == null || candiUser.trim().length() == 0)
//                {
//                    throw new ServerSideBusinessException("用户不存在！", HttpStatus.NOT_ACCEPTABLE);
//                }
//
//                variables.put(ServerConstantValue.ACT_FW_STG_3_CANDI_USERS,candiUser);
//                variables.put(ServerConstantValue.ACT_FW_STG_3_CANDI_GROUPS,String.valueOf(group.get().getId()));
//            }
//            else if (orderStatus == OrderStatus.OSCompleted.getIndex())//结束
//            {
//                // to don nothing
//            }
//            else
//            {
//                throw new ServerSideBusinessException("查询参数错误，订单状态不正确！", HttpStatus.NOT_ACCEPTABLE);
//            }
//
//            Order newOrder = new Order();
//            newOrder.setId(orderOptional.get().getId());
//            newOrder.setOrderSn(orderOptional.get().getOrderSn());
//            newOrder.setOrderStatus(orderStatus);
//            orderService.update(taskId,variables,orderOptional.get().getId(),newOrder,forceDispatch);
//        }
//
//        return ResponseEntity.ok().build();
//    }


    @RequestMapping(value = "/api/TaskOrders/OpHistory", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "获取任务订单更改历史")
    public ResponseEntity retrieveTaskOrderOpHis(@RequestParam(value = "orderSn", defaultValue = "") String orderSn,
                                                 @RequestParam(value = "userId", defaultValue = "") String userId,
                                                 @RequestParam(value = "orderStatus", required = false) OrderStatus orderStatus,
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

        if (orderStatus != null)
        {
            params.putAll(ImmutableMap.of("orderStatus", orderStatus.getIndex()));
        }


        params.putAll(paginationParams(pageNo, pageSize, orderBy));

        List<OrderOpHistory> orders = orderOpHistoryService.retrieve(params);
        Integer count = orderOpHistoryService.count(params);
        return ResponseEntity.ok(ListRep.assemble(orders, count));
    }


    @RequestMapping(value = "/api/Orders", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "获取订单列表")
    public ResponseEntity retrieve(@RequestParam(value = "orderSn", defaultValue = "") String orderSn,
                                   @RequestParam(value = "callInPhone", defaultValue = "") String callInPhone,
                                   @RequestParam(value = "userId", defaultValue = "") String userId,
                                   @RequestParam(value = "orderStatus", required = false) Integer orderStatus,
                                   @RequestParam(value = "payStatus", required = false) PayStatus payStatus,
                                   @RequestParam(value = "payType", required = false) PayType payType,
                                   @RequestParam(value = "orderServiceQuality", required = false) OrderServiceQuality orderServiceQuality,
                                   @RequestParam(value = "invoiceStatus", required = false) Integer invoiceStatus,
                                   @RequestParam(value = "accessType", required = false) AccessType accessType,
                                   @RequestParam(value = "addrProvince", defaultValue = "") String addrProvince,
                                   @RequestParam(value = "addrCity", defaultValue = "") String addrCity,
                                   @RequestParam(value = "addrCounty", defaultValue = "") String addrCounty,
                                   @RequestParam(value = "addrDetail", defaultValue = "") String addrDetail,
                                   @RequestParam(value = "recvName", defaultValue = "") String recvName,
                                   @RequestParam(value = "recvPhone", defaultValue = "") String recvPhone,
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

        if (callInPhone.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("callInPhone", callInPhone));
        }

        if (userId.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("userId", userId));
        }

        if (orderStatus != null)
        {
            params.putAll(ImmutableMap.of("orderStatus", orderStatus));
        }

        if (invoiceStatus != null)
        {
            params.putAll(ImmutableMap.of("invoiceStatus", invoiceStatus));
        }

        if (payStatus != null)
        {
            params.putAll(ImmutableMap.of("payStatus", payStatus));
        }

        if (orderServiceQuality != null)
        {
            params.putAll(ImmutableMap.of("orderServiceQuality", orderServiceQuality));
        }



        if (payType != null)
        {
            params.putAll(ImmutableMap.of("payType", payType));
        }

        if (accessType != null)
        {
            params.putAll(ImmutableMap.of("accessType", accessType));
        }

        if (addrProvince.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("addrProvince", addrProvince));
        }

        if (addrCity.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("addrCity", addrCity));
        }

        if (addrCounty.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("addrCounty", addrCounty));
        }

        if (addrDetail.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("addrDetail", addrDetail));
        }

        if (recvName.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("recvName", recvName));
        }

        if (recvPhone.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("recvPhone", recvPhone));
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

        if (departmentCode.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("departmentCode", departmentCode));
        }

        params.putAll(paginationParams(pageNo, pageSize, orderBy));

        List<Order> orders = orderService.retrieve(params);
        Integer count = orderService.count(params);
        return ResponseEntity.ok(ListRep.assemble(orders, count));
    }

    @OperationLog(desc = "创建订单")
    @RequestMapping(value = "/api/Orders", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody Order order, UriComponentsBuilder ucBuilder)
    {
        ResponseEntity responseEntity;

        /*创建订单入库*/
        orderService.create(order);

        URI uri = ucBuilder.path("/api/Orders/{orderSn}").buildAndExpand(order.getOrderSn()).toUri();
        responseEntity = ResponseEntity.created(uri).build();

        return responseEntity;
    }

    @OperationLog(desc = "订单关联钢瓶号")
    @RequestMapping(value = "/api/Orders/Bind/GasCynNumber", method = RequestMethod.PUT)
    public ResponseEntity orderBindGasCynNumber(@RequestParam(value = "orderSn", defaultValue = "") String orderSn,
                                   @RequestParam(value = "gasCynNumbers", defaultValue = "") String gasCynNumbers
    )
    {
        ResponseEntity responseEntity;

        orderService.orderBindGasCynNumber(orderSn,gasCynNumbers);

        responseEntity = ResponseEntity.ok().build();

        return responseEntity;
    }

    @OperationLog(desc = "计算订单价格")
    @RequestMapping(value = "/api/Orders/Caculate/{orderSn}", method = RequestMethod.PUT)
    public ResponseEntity caculate(@PathVariable("orderSn") String orderSn,
                                   @RequestParam(value = "customerId", defaultValue = "") String customerId,
                                   @RequestBody List<OrderCaculator> orderCaculators
    )
    {
        ResponseEntity responseEntity;

        List<GasCalcResult> gasCalcResults = orderService.caculate(orderSn,customerId,orderCaculators);

        responseEntity = ResponseEntity.ok(ListRep.assemble(gasCalcResults, gasCalcResults.size()));

        return responseEntity;
    }


//    @RequestMapping(value = "/api/Orders/Test", method = RequestMethod.PUT)
//    public ResponseEntity Test(@RequestBody List<Order> orderList)
//    {
//        ResponseEntity responseEntity;
//
//        responseEntity = ResponseEntity.ok(ListRep.assemble(orderList, orderList.size()));
//
//        return responseEntity;
//    }


    @OperationLog(desc = "气票用户支付")
    @RequestMapping(value = "/api/TicketOrders/{orderSn}", method = RequestMethod.PUT)
    public ResponseEntity ticketPay(@PathVariable("orderSn") String orderSn,
                                    @RequestParam(value = "coupons", defaultValue = "") String coupons,
                                    @RequestParam(value = "tickets", defaultValue = "") String tickets
                                    )
    {
        ResponseEntity responseEntity = ResponseEntity.ok().build();

        Optional<Order> orderOptional = orderService.findBySn(orderSn);
        if (orderOptional.isPresent())
        {
            if (orderOptional.get().getPayStatus() == PayStatus.PSUnpaid)
            {
                orderService.ticketPay(orderOptional.get(),coupons,tickets);
                responseEntity = ResponseEntity.ok().build();
            }
            else if (orderOptional.get().getPayStatus() == PayStatus.PSUnpaid)
            {
                throw new ServerSideBusinessException("定单已支付，请勿重复支付！", HttpStatus.NOT_ACCEPTABLE);
            }
            else if (orderOptional.get().getPayStatus() == PayStatus.PSRefounding)
            {
                throw new ServerSideBusinessException("定单处于退款状态！", HttpStatus.NOT_ACCEPTABLE);
            }
            else if (orderOptional.get().getPayStatus() == PayStatus.PSRefounded)
            {
                throw new ServerSideBusinessException("定单已退款！", HttpStatus.NOT_ACCEPTABLE);
            }
        }
        else
        {
            responseEntity = ResponseEntity.notFound().build();
        }

        return responseEntity;
    }

    @OperationLog(desc = "修改订单信息")
    @RequestMapping(value = "/api/Orders/{orderSn}", method = RequestMethod.PUT)
    public ResponseEntity update(@PathVariable("orderSn") String orderSn, @RequestBody Order newOrder)
    {
        ResponseEntity responseEntity;

        Optional<Order> orderOptional = orderService.findBySn(orderSn);
        if (orderOptional.isPresent())
        {
            orderService.update(orderOptional.get(),newOrder);
            responseEntity = ResponseEntity.ok().build();
        }
        else
        {
            responseEntity = ResponseEntity.notFound().build();
        }

        return responseEntity;
    }

    @OperationLog(desc = "订单支付(普通用户--扫码，现金，赊销；月结用户)")
    @RequestMapping(value = "/api/orderPay/{orderSn}", method = RequestMethod.PUT)
    public ResponseEntity orderPay(@PathVariable("orderSn") String orderSn,
                                   @RequestParam(value = "payType", required = true) PayType payType
    )
    {
        ResponseEntity responseEntity = ResponseEntity.notFound().build();

        Optional<Order> orderOptional = orderService.findBySn(orderSn);
        if (orderOptional.isPresent())
        {
            if (orderOptional.get().getPayStatus() == PayStatus.PSUnpaid)
            {
                orderService.orderPay(orderOptional.get(),payType);
                responseEntity = ResponseEntity.ok().build();
            }
            else if (orderOptional.get().getPayStatus() == PayStatus.PSPaied)
            {
                throw new ServerSideBusinessException("定单已支付，请勿重复支付！", HttpStatus.NOT_ACCEPTABLE);
            }
            else if (orderOptional.get().getPayStatus() == PayStatus.PSRefounding)
            {
                throw new ServerSideBusinessException("定单处于退款状态！", HttpStatus.NOT_ACCEPTABLE);
            }
            else if (orderOptional.get().getPayStatus() == PayStatus.PSRefounded)
            {
                throw new ServerSideBusinessException("定单已退款！", HttpStatus.NOT_ACCEPTABLE);
            }
        }
        else
        {
            responseEntity = ResponseEntity.notFound().build();
        }

        return responseEntity;
    }


    @OperationLog(desc = "删除订单信息")
    @RequestMapping(value = "/api/Orders/{orderSn}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable("orderSn") String orderSn)
    {
        ResponseEntity responseEntity;

        Optional<Order> orderOptional = orderService.findBySn(orderSn);
        if (orderOptional.isPresent())
        {
            orderService.deleteById(orderOptional.get().getId());
            responseEntity = ResponseEntity.noContent().build();
        }
        else
        {
            responseEntity = ResponseEntity.notFound().build();
        }

        return responseEntity;
    }


    @OperationLog(desc = "订单作废")
    @RequestMapping(value = "/api/CancelOrder/{orderSn}", method = RequestMethod.PUT)
    public ResponseEntity cancelOrder(@PathVariable("orderSn") String orderSn)
    {
        ResponseEntity responseEntity;

        Optional<Order> orderOptional = orderService.findBySn(orderSn);
        if (orderOptional.isPresent())
        {
            orderService.cancelOrder(orderSn);
            responseEntity = ResponseEntity.noContent().build();
        }
        else
        {
            responseEntity = ResponseEntity.notFound().build();
        }

        return responseEntity;
    }
}

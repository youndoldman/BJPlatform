package com.donno.nj.controller;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.constant.Constant;
import com.donno.nj.domain.*;
import com.donno.nj.activiti.WorkFlowTypes;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.representation.ListRep;
import com.donno.nj.representation.MapRep;
import com.donno.nj.util.DistanceHelper;
import com.donno.nj.service.*;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Interner;
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
public class OrderController
{
    @Autowired
    private OrderService orderService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private WorkFlowService workFlowService;

    @Autowired
    private GroupService groupService;


    @RequestMapping(value = "/api/Orders/{userId}", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "获取任务订单列表")
    public ResponseEntity retrieveTaskOrder(@PathVariable("userId") String userId,
                                   @RequestParam(value = "orderBy", defaultValue = "") String orderBy,
                                   @RequestParam(value = "pageSize", defaultValue = Constant.PAGE_SIZE) Integer pageSize,
                                   @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo)
    {
        Map params = new HashMap<String,String>();
        params.putAll(ImmutableMap.of("userId", userId));

        Map mapOrder = new HashMap<Interner,Order>();//taskid+order

        List<Task> taskList = workFlowService.getTasksByUserId(userId);
        for (Task task:taskList)
        {
            Optional<Order> order = orderService.findBySn(task.getProcess().getBuinessKey());
            if (order.isPresent())
            {
                mapOrder.put(task.getId(),order.get());
            }
        }

        Integer count = orderService.count(params);
        return ResponseEntity.ok(MapRep.assemble(mapOrder, mapOrder.size()));
    }



    @RequestMapping(value = "/api/Orders", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "获取订单列表")
    public ResponseEntity retrieve(@RequestParam(value = "orderSn", defaultValue = "") String orderSn,
                                   @RequestParam(value = "callInPhone", defaultValue = "") String callInPhone,
                                   @RequestParam(value = "userId", defaultValue = "") String userId,
                                   @RequestParam(value = "orderStatus", required = false) Integer orderStatus,
                                   @RequestParam(value = "addrProvince", defaultValue = "") String addrProvince,
                                   @RequestParam(value = "addrCity", defaultValue = "") String addrCity,
                                   @RequestParam(value = "addrCounty", defaultValue = "") String addrCounty,
                                   @RequestParam(value = "addrDetail", defaultValue = "") String addrDetail,
                                   @RequestParam(value = "recvName", defaultValue = "") String recvName,
                                   @RequestParam(value = "recvPhone", defaultValue = "") String recvPhone,
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

        /*启动流程*/
        Map<String, Object> variables = new HashMap<String, Object>();

        Optional<Group>  group = groupService.findByCode(ServerConstantValue.GP_CUSTOMER_SERVICE);
        if(group.isPresent())
        {
            //variables.put(ServerConstantValue.ACT_FW_STG_ASSIGN_USERS,"");

            /*指定可办理的组*/
            variables.put(ServerConstantValue.ACT_FW_STG_CANDI_GROUPS,String.valueOf(group.get().getId()));

            /*指定可办理该流程用户,根据经纬度寻找合适的派送工*/
            Map findDispatchParams = new HashMap<String,String>();
            findDispatchParams.putAll(ImmutableMap.of("groupCode", ServerConstantValue.GP_DISPATCH));
            List<SysUser> sysUsersList = sysUserService.retrieve(findDispatchParams);
            if (sysUsersList.size() > 0)
            {
                String candUser = "";
                for (SysUser sysUser:sysUsersList)
                {
                    if (sysUser.getUserPosition() != null)
                    {
                        Double distance = DistanceHelper.Distance(order.getRecvLatitude(),order.getRecvLongitude(),sysUser.getUserPosition().getLatitude(),sysUser.getUserPosition().getLongitude());
                        if ( distance < 5)
                        {
                            if (candUser.trim().length() > 0 )
                            {
                                candUser = candUser + ",";
                            }
                            candUser = candUser + sysUser.getUserId();
                        }
                    }
                }

                variables.put(ServerConstantValue.ACT_FW_STG_CANDI_USERS,candUser);
            }

            workFlowService.createWorkFlow(WorkFlowTypes.GAS_ORDER_FLOW,order.getCustomer().getUserId(),variables,order.getOrderSn());
        }
        else
        {
            orderService.deleteById(order.getId());
            throw new ServerSideBusinessException("派单失败！", HttpStatus.NOT_ACCEPTABLE);
        }


        URI uri = ucBuilder.path("/api/Orders/{id}").buildAndExpand(order.getId()).toUri();
        responseEntity = ResponseEntity.created(uri).build();

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
            orderService.update(orderOptional.get().getId(),newOrder);
            responseEntity = ResponseEntity.ok().build();
        }
        else
        {
            responseEntity = ResponseEntity.notFound().build();
        }

        return responseEntity;
    }

    @OperationLog(desc = "删除商品信息")
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

}

package com.donno.nj.controller;

import com.donno.nj.aspect.Auth;
import com.donno.nj.aspect.OperationLog;
import com.donno.nj.constant.Constant;
import com.donno.nj.domain.*;
import com.donno.nj.representation.ListRep;
import com.donno.nj.service.AdviceService;
import com.donno.nj.service.ElectDepositService;
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
public class ElectDepositController
{
    @Autowired
    ElectDepositService electDepositService ;

    @RequestMapping(value = "/api/ElectDeposit", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "获取电子押金单列表")
    public ResponseEntity retrieve(@RequestParam(value = "customerId", defaultValue = "") String customerId,
                                   @RequestParam(value = "depositSn", defaultValue = "") String depositSn,
                                   @RequestParam(value = "dispatchId", defaultValue = "") String dispatchId,
                                   @RequestParam(value = "departmentCode", defaultValue = "") String departmentCode,
                                   @RequestParam(value = "electDepositStatus", required = false ) ElectDepositStatus electDepositStatus,
                                   @RequestParam(value = "startTime", defaultValue = "") String startTime,
                                   @RequestParam(value = "endTime", defaultValue = "") String endTime,
                                   @RequestParam(value = "orderBy", defaultValue = "") String orderBy,
                                   @RequestParam(value = "pageSize", defaultValue = Constant.PAGE_SIZE) Integer pageSize,
                                   @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo)
    {
        Map params = new HashMap<String,String>();

        if (customerId.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("customerId", customerId));
        }

        if (depositSn.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("depositSn", depositSn));
        }

        if (dispatchId.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("dispatchId", dispatchId));
        }

        if (departmentCode.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("departmentCode", departmentCode));
        }

        if (electDepositStatus != null)
        {
            params.putAll(ImmutableMap.of("electDepositStatus", electDepositStatus.getIndex()));
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






        List<ElectDeposit> electDeposits = electDepositService.retrieve(params);
        Integer count = electDepositService.count(params);

        return ResponseEntity.ok(ListRep.assemble(electDeposits, count));
    }


    @OperationLog(desc = "增加电子押金单信息")
    @RequestMapping(value = "/api/ElectDeposit", method = RequestMethod.POST)
    //@Auth(allowedBizOp = {ServerConstantValue.GP_ADMIN,ServerConstantValue.GP_CUSTOMER_SERVICE })
    public ResponseEntity create(@RequestBody ElectDeposit electDeposit,
                                 UriComponentsBuilder ucBuilder)
    {
        ResponseEntity responseEntity;

        /*创建*/
        electDepositService.create(electDeposit);

        URI uri = ucBuilder.path("/api/ElectDeposit/{id}").buildAndExpand(electDeposit.getId()).toUri();
        responseEntity = ResponseEntity.created(uri).build();

        return responseEntity;
    }


    @OperationLog(desc = "修改电子押金单信息")
    @RequestMapping(value = "/api/ElectDeposit/{id}", method = RequestMethod.PUT)
    //@Auth(allowedBizOp = {ServerConstantValue.GP_ADMIN,ServerConstantValue.GP_CUSTOMER_SERVICE })
    public ResponseEntity update(@PathVariable("id") Integer id,
                                 @RequestBody ElectDeposit electDeposit)
    {
        ResponseEntity responseEntity;

        electDepositService.update(id, electDeposit);

        responseEntity = ResponseEntity.ok().build();

        return responseEntity;
    }



    @OperationLog(desc = "删除电子押金单信息")
    @RequestMapping(value = "/api/ElectDeposit/{id}", method = RequestMethod.DELETE)
    @Auth(allowedBizOp = {ServerConstantValue.GP_ADMIN,ServerConstantValue.GP_CUSTOMER_SERVICE })
    public ResponseEntity delete(@PathVariable("id") Integer id)
    {
        ResponseEntity responseEntity;

        electDepositService.deleteById(id);

        responseEntity = ResponseEntity.noContent().build();

        return responseEntity;
    }
}

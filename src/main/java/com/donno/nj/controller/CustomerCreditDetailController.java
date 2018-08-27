package com.donno.nj.controller;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.constant.Constant;
import com.donno.nj.domain.CustomerCredit;
import com.donno.nj.domain.CustomerCreditDetail;
import com.donno.nj.representation.ListRep;
import com.donno.nj.service.CustomerCreditDetailService;
import com.donno.nj.service.CustomerCreditService;
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
public class CustomerCreditDetailController
{

    @Autowired
    CustomerCreditDetailService customerCreditDetailService ;


    @RequestMapping(value = "/api/CustomerCreditDetail", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "获取信用信息列表")
    public ResponseEntity retrieve(
                                   @RequestParam(value = "userId", defaultValue = "") String userId,
                                   @RequestParam(value = "creditType", required = false) Integer creditType,
                                   @RequestParam(value = "startTime", defaultValue = "") String startTime,
                                   @RequestParam(value = "endTime", defaultValue = "") String endTime,
                                   @RequestParam(value = "orderBy", defaultValue = "") String orderBy,
                                   @RequestParam(value = "pageSize", defaultValue = Constant.PAGE_SIZE) Integer pageSize,
                                   @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo)
    {
        Map params = new HashMap<String,String>();

        if (userId.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("userId", userId));
        }

        if (creditType != null)
        {
            params.putAll(ImmutableMap.of("creditType", creditType));
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

        List<CustomerCreditDetail> customerCreditDetails = customerCreditDetailService.retrieve(params);
        Integer count = customerCreditDetailService.count(params);

        return ResponseEntity.ok(ListRep.assemble(customerCreditDetails, count));
    }


    @OperationLog(desc = "增加客户欠款信息")
    @RequestMapping(value = "/api/CustomerCreditDetail", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody CustomerCreditDetail customerCreditDetail, UriComponentsBuilder ucBuilder)
    {
        ResponseEntity responseEntity;

        /*创建*/
        customerCreditDetailService.create(customerCreditDetail);

        URI uri = ucBuilder.path("/api/CustomerCreditDetail/{id}").buildAndExpand(customerCreditDetail.getId()).toUri();
        responseEntity = ResponseEntity.created(uri).build();

        return responseEntity;
    }



    @OperationLog(desc = "删除客户欠款信息")
    @RequestMapping(value = "/api/CustomerCreditDetail/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable("id") Integer id)
    {
        ResponseEntity responseEntity;

        customerCreditDetailService.deleteById(id);

        responseEntity = ResponseEntity.noContent().build();

        return responseEntity;
    }
}

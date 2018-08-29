package com.donno.nj.controller;

import com.donno.nj.aspect.Auth;
import com.donno.nj.aspect.OperationLog;
import com.donno.nj.constant.Constant;
import com.donno.nj.domain.CustomerCredit;
import com.donno.nj.domain.ServerConstantValue;
import com.donno.nj.representation.ListRep;
import com.donno.nj.service.CustomerCreditService;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.donno.nj.util.ParamMapBuilder.paginationParams;

@RestController
public class CustomerCreditController
{

    @Autowired
    CustomerCreditService customerCreditService ;


    @RequestMapping(value = "/api/CustomerCredit", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "获取欠款信息列表")
    //@Auth(allowedBizOp = {})
    public ResponseEntity retrieve(
                                   @RequestParam(value = "userId", defaultValue = "") String userId,
                                   @RequestParam(value = "creditType", required = false) Integer creditType,
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

        params.putAll(paginationParams(pageNo, pageSize, orderBy));

        List<CustomerCredit> customerCredits = customerCreditService.retrieve(params);
        Integer count = customerCreditService.count(params);

        return ResponseEntity.ok(ListRep.assemble(customerCredits, count));
    }


    @OperationLog(desc = "删除客户欠款信息")
    @RequestMapping(value = "/api/CustomerCredit/{id}", method = RequestMethod.DELETE)
    //@Auth(allowedBizOp = {ServerConstantValue.GP_ADMIN})
    public ResponseEntity delete(@PathVariable("id") Integer id)
    {
        ResponseEntity responseEntity;

        customerCreditService.deleteById(id);

        responseEntity = ResponseEntity.noContent().build();

        return responseEntity;
    }
}

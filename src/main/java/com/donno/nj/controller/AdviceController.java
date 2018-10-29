package com.donno.nj.controller;

import com.donno.nj.aspect.Auth;
import com.donno.nj.aspect.OperationLog;
import com.donno.nj.constant.Constant;
import com.donno.nj.domain.Advice;
import com.donno.nj.domain.ServerConstantValue;
import com.donno.nj.domain.Ticket;
import com.donno.nj.representation.ListRep;
import com.donno.nj.service.AdviceService;
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
public class AdviceController
{
    @Autowired
    AdviceService adviceService ;

    @RequestMapping(value = "/api/Advice", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "获取客户建议信息列表")
    public ResponseEntity retrieve(@RequestParam(value = "userId", defaultValue = "") String userId,
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

        if (startTime.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("startTime", startTime));
        }

        if (endTime.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("endTime", endTime));
        }

        params.putAll(paginationParams(pageNo, pageSize, orderBy));

        List<Advice> advices = adviceService.retrieve(params);
        Integer count = adviceService.count(params);

        return ResponseEntity.ok(ListRep.assemble(advices, count));
    }


    @OperationLog(desc = "增加客户建议信息")
    @RequestMapping(value = "/api/Advice", method = RequestMethod.POST)
    @Auth(allowedBizOp = {ServerConstantValue.GP_ADMIN,ServerConstantValue.GP_CUSTOMER_SERVICE })
    public ResponseEntity create(@RequestBody Advice advice,
                                 UriComponentsBuilder ucBuilder)
    {
        ResponseEntity responseEntity;

        /*创建*/
        adviceService.create(advice);

        URI uri = ucBuilder.path("/api/Advice/{id}").buildAndExpand(advice.getId()).toUri();
        responseEntity = ResponseEntity.created(uri).build();

        return responseEntity;
    }


    @OperationLog(desc = "修改客户建议信息")
    @RequestMapping(value = "/api/Advice/{id}", method = RequestMethod.PUT)
    @Auth(allowedBizOp = {ServerConstantValue.GP_ADMIN,ServerConstantValue.GP_CUSTOMER_SERVICE })
    public ResponseEntity update(@PathVariable("id") Integer id,
                                 @RequestBody Advice advice)
    {
        ResponseEntity responseEntity;

        adviceService.update(id, advice);

        responseEntity = ResponseEntity.ok().build();

        return responseEntity;
    }



    @OperationLog(desc = "删除客户建议信息")
    @RequestMapping(value = "/api/Advice/{id}", method = RequestMethod.DELETE)
    @Auth(allowedBizOp = {ServerConstantValue.GP_ADMIN,ServerConstantValue.GP_CUSTOMER_SERVICE })
    public ResponseEntity delete(@PathVariable("id") Integer id)
    {
        ResponseEntity responseEntity;

        adviceService.deleteById(id);

        responseEntity = ResponseEntity.noContent().build();

        return responseEntity;
    }
}

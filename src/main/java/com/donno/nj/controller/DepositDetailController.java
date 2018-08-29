package com.donno.nj.controller;

import com.donno.nj.aspect.Auth;
import com.donno.nj.aspect.OperationLog;
import com.donno.nj.constant.Constant;
import com.donno.nj.domain.DepositDetail;
import com.donno.nj.domain.ServerConstantValue;
import com.donno.nj.domain.WriteOffDetail;
import com.donno.nj.representation.ListRep;
import com.donno.nj.service.DepositDetailService;
import com.donno.nj.service.WriteOffDetailService;
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
public class DepositDetailController
{
    @Autowired
    DepositDetailService depositDetailService ;

    @RequestMapping(value = "/api/DepositDetail", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "获取入银行款记录信息列表")
    //@Auth(allowedBizOp = {})
    public ResponseEntity retrieve(
                                   @RequestParam(value = "userId", defaultValue = "") String userId,
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

        List<DepositDetail> depositDetails = depositDetailService.retrieve(params);
        Integer count = depositDetailService.count(params);

        return ResponseEntity.ok(ListRep.assemble(depositDetails, count));
    }


    @OperationLog(desc = "增加入银行款记录信息")
    @RequestMapping(value = "/api/DepositDetail", method = RequestMethod.POST)
    //@Auth(allowedBizOp = {ServerConstantValue.GP_ADMIN ,ServerConstantValue.GP_FINANCE})
    public ResponseEntity create(@RequestBody DepositDetail depositDetail, UriComponentsBuilder ucBuilder)
    {
        ResponseEntity responseEntity;

        /*创建*/
        depositDetailService.create(depositDetail);

        URI uri = ucBuilder.path("/api/DepositDetail/{id}").buildAndExpand(depositDetail.getId()).toUri();
        responseEntity = ResponseEntity.created(uri).build();

        return responseEntity;
    }


    @OperationLog(desc = "删除入银行款记录信息")
    @RequestMapping(value = "/api/DepositDetail/{id}", method = RequestMethod.DELETE)
   // @Auth(allowedBizOp = {ServerConstantValue.GP_ADMIN ,ServerConstantValue.GP_FINANCE})
    public ResponseEntity delete(@PathVariable("id") Integer id)
    {
        ResponseEntity responseEntity;

        depositDetailService.deleteById(id);

        responseEntity = ResponseEntity.noContent().build();

        return responseEntity;
    }

}

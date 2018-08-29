package com.donno.nj.controller;

import com.donno.nj.aspect.Auth;
import com.donno.nj.aspect.OperationLog;
import com.donno.nj.constant.Constant;
import com.donno.nj.domain.GasCyrChargeSpec;
import com.donno.nj.domain.Ticket;
import com.donno.nj.representation.ListRep;
import com.donno.nj.service.GasCyrChargeSpecService;
import com.donno.nj.service.TicketService;
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
public class GasCyrChargeSpecController
{
    @Autowired
    GasCyrChargeSpecService gasCyrChargeSpecService ;

    @RequestMapping(value = "/api/GasCyrChargeSpec", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "查询钢瓶收费标准信息数量")
    public ResponseEntity retrieve(@RequestParam(value = "gasCyrSpecCode", defaultValue = "") String gasCyrSpecCode,
                                   @RequestParam(value = "chargeType", required = true) Integer chargeType,
                                   @RequestParam(value = "startTime", defaultValue = "") String startTime,
                                   @RequestParam(value = "endTime", defaultValue = "") String endTime,
                                   @RequestParam(value = "orderBy", defaultValue = "") String orderBy,
                                   @RequestParam(value = "pageSize", defaultValue = Constant.PAGE_SIZE) Integer pageSize,
                                   @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo)
    {
        Map params = new HashMap<String,String>();

        if (gasCyrSpecCode.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("gasCyrSpecCode", gasCyrSpecCode));
        }


        if (startTime.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("startTime", startTime));
        }


        if (endTime.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("endTime", endTime));
        }

        if (chargeType != null)
        {
            params.putAll(ImmutableMap.of("chargeType", chargeType));
        }

        params.putAll(paginationParams(pageNo, pageSize, orderBy));

        List<GasCyrChargeSpec> gasCyrChargeSpecs = gasCyrChargeSpecService.retrieve(params);
        Integer count = gasCyrChargeSpecService.count(params);

        return ResponseEntity.ok(ListRep.assemble(gasCyrChargeSpecs, count));
    }


    @OperationLog(desc = "增加钢瓶收费标准信息")
    @RequestMapping(value = "/api/GasCyrChargeSpec", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody GasCyrChargeSpec gasCyrChargeSpec, UriComponentsBuilder ucBuilder)
    {
        ResponseEntity responseEntity;

        /*创建*/
        gasCyrChargeSpecService.create(gasCyrChargeSpec);

        URI uri = ucBuilder.path("/api/GasCyrChargeSpec/{id}").buildAndExpand(gasCyrChargeSpec.getId()).toUri();
        responseEntity = ResponseEntity.created(uri).build();

        return responseEntity;
    }


    @OperationLog(desc = "修改钢瓶收费标准信息")
    @RequestMapping(value = "/api/GasCyrChargeSpec/{id}", method = RequestMethod.PUT)
    public ResponseEntity update(@PathVariable("id") Integer id,
                                 @RequestBody GasCyrChargeSpec newGasCyrChargeSpec)
    {
        ResponseEntity responseEntity;

        gasCyrChargeSpecService.update(id, newGasCyrChargeSpec);

        responseEntity = ResponseEntity.ok().build();

        return responseEntity;
    }



    @OperationLog(desc = "删除钢瓶收费标准信息")
    @RequestMapping(value = "/api/GasCyrChargeSpec/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable("id") Integer id)
    {
        ResponseEntity responseEntity;

        gasCyrChargeSpecService.deleteById(id);

        responseEntity = ResponseEntity.noContent().build();

        return responseEntity;
    }
}

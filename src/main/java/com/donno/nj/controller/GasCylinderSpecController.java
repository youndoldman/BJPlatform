package com.donno.nj.controller;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.constant.Constant;
import com.donno.nj.domain.GasCylinderSpec;
import com.donno.nj.representation.ListRep;
import com.donno.nj.service.GasCylinderSpecService;
import com.google.common.collect.ImmutableMap;
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
public class GasCylinderSpecController
{

    @Autowired
    GasCylinderSpecService gasCylinderSpecService;

    @RequestMapping(value = "/api/GasCylinderSpec", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "获取钢瓶规格列表")
    public ResponseEntity retrieve(@RequestParam(value = "code", defaultValue = "") String code,
                                            @RequestParam(value = "name", defaultValue = "") String name,
                                            @RequestParam(value = "orderBy", defaultValue = "") String orderBy,
                                            @RequestParam(value = "pageSize", defaultValue = Constant.PAGE_SIZE) Integer pageSize,
                                            @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo)
    {
        Map params = new HashMap<String,String>();
        if (code.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("code", code));
        }

        if (name.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("name", name));
        }

        params.putAll(paginationParams(pageNo, pageSize, orderBy));

        List<GasCylinderSpec> customers = gasCylinderSpecService.retrieve(params);
        Integer count = gasCylinderSpecService.count(params);

        return ResponseEntity.ok(ListRep.assemble(customers, count));
    }

    @OperationLog(desc = "创建钢瓶规格")
    @RequestMapping(value = "/api/GasCylinderSpec", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody GasCylinderSpec gasCylinderSpec, UriComponentsBuilder ucBuilder)
    {
        ResponseEntity responseEntity;
        if (gasCylinderSpecService.findByCode(gasCylinderSpec.getCode()).isPresent())
        {
            responseEntity =  ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        else
        {
             /*创建*/
            gasCylinderSpecService.create(gasCylinderSpec);

            URI uri = ucBuilder.path("/api/GasCylinderSpec/{code}").buildAndExpand(gasCylinderSpec.getCode()).toUri();
            responseEntity = ResponseEntity.created(uri).build();
        }

        return responseEntity;
    }

    @OperationLog(desc = "修改钢瓶规格信息")
    @RequestMapping(value = "/api/GasCylinderSpec/{code}", method = RequestMethod.PUT)
    public ResponseEntity update(@PathVariable("code") String code, @RequestBody GasCylinderSpec newGasCylinderSpec)
    {
        ResponseEntity responseEntity;

        gasCylinderSpecService.update(code, newGasCylinderSpec);
        responseEntity = ResponseEntity.ok().build();

        return responseEntity;
    }

    @OperationLog(desc = "删除钢瓶规格信息")
    @RequestMapping(value = "/api/GasCylinderSpec/{code}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable("code") String code)
    {
        ResponseEntity responseEntity;

        gasCylinderSpecService.deleteByCode(code);
        responseEntity = ResponseEntity.noContent().build();

        return responseEntity;
    }
}
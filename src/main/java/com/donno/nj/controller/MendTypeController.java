package com.donno.nj.controller;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.constant.Constant;
import com.donno.nj.domain.MendType;
import com.donno.nj.representation.ListRep;
import com.donno.nj.service.MendTypeService;
import com.google.common.collect.ImmutableMap;
import com.google.common.net.MediaType;
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
public class MendTypeController
{

    @Autowired
    MendTypeService mendTypeService;

    @RequestMapping(value = "/api/MendTypes", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "获取报修类型列表")
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

        List<MendType> mendTypes = mendTypeService.retrieve(params);
        Integer count = mendTypeService.count(params);

        return ResponseEntity.ok(ListRep.assemble(mendTypes, count));
    }

    @OperationLog(desc = "创建报修类型")
    @RequestMapping(value = "/api/MendTypes", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody MendType mendType, UriComponentsBuilder ucBuilder)
    {
        ResponseEntity responseEntity;
        if (mendTypeService.findByCode(mendType.getCode()).isPresent())
        {
            responseEntity =  ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        else
        {
             /*创建*/
            mendTypeService.create(mendType);

            URI uri = ucBuilder.path("/api/MendTypes/{code}").buildAndExpand(mendType.getCode()).toUri();
            responseEntity = ResponseEntity.created(uri).build();
        }

        return responseEntity;
    }

    @OperationLog(desc = "修改报修类型信息")
    @RequestMapping(value = "/api/MendTypes/{code}", method = RequestMethod.PUT)
    public ResponseEntity update(@PathVariable("code") String code, @RequestBody MendType newMendType)
    {
        ResponseEntity responseEntity;

        mendTypeService.update(code, newMendType);
        responseEntity = ResponseEntity.ok().build();

        return responseEntity;
    }

    @OperationLog(desc = "删除报修类型信息")
    @RequestMapping(value = "/api/MendTypes/{code}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable("code") String code)
    {
        ResponseEntity responseEntity;

        mendTypeService.deleteByCode(code);
        responseEntity = ResponseEntity.noContent().build();

        return responseEntity;
    }
}

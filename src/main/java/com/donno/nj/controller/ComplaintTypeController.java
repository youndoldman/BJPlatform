package com.donno.nj.controller;

import com.donno.nj.aspect.Auth;
import com.donno.nj.aspect.OperationLog;
import com.donno.nj.constant.Constant;
import com.donno.nj.domain.ComplaintType;
import com.donno.nj.domain.ServerConstantValue;
import com.donno.nj.representation.ListRep;
import com.donno.nj.service.ComplaintTypeService;
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
public class ComplaintTypeController
{

    @Autowired
    ComplaintTypeService complaintTypeService;

    @RequestMapping(value = "/api/ComplaintTypes", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "获取投诉类型列表")
    //@Auth(allowedBizOp = {})
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

        List<ComplaintType> complaintTypes = complaintTypeService.retrieve(params);
        Integer count = complaintTypeService.count(params);

        return ResponseEntity.ok(ListRep.assemble(complaintTypes, count));
    }

    @OperationLog(desc = "创建投诉类型")
    @RequestMapping(value = "/api/ComplaintTypes", method = RequestMethod.POST)
    //@Auth(allowedBizOp = {ServerConstantValue.GP_ADMIN ,ServerConstantValue.GP_CUSTOMER_SERVICE,ServerConstantValue.GP_GAS_STORE_LEADER})
    public ResponseEntity create(@RequestBody ComplaintType complaintType, UriComponentsBuilder ucBuilder)
    {
        ResponseEntity responseEntity;
        if (complaintTypeService.findByCode(complaintType.getCode()).isPresent())
        {
            responseEntity =  ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        else
        {
             /*创建*/
            complaintTypeService.create(complaintType);

            URI uri = ucBuilder.path("/api/complaintTypes/{code}").buildAndExpand(complaintType.getCode()).toUri();
            responseEntity = ResponseEntity.created(uri).build();
        }

        return responseEntity;
    }

    @OperationLog(desc = "修改投诉类型信息")
    @RequestMapping(value = "/api/ComplaintTypes/{code}", method = RequestMethod.PUT)
    //@Auth(allowedBizOp = {ServerConstantValue.GP_ADMIN ,ServerConstantValue.GP_CUSTOMER_SERVICE,ServerConstantValue.GP_GAS_STORE_LEADER})
    public ResponseEntity update(@PathVariable("code") String code, @RequestBody ComplaintType newcomplaintType)
    {
        ResponseEntity responseEntity;

        complaintTypeService.update(code, newcomplaintType);
        responseEntity = ResponseEntity.ok().build();

        return responseEntity;
    }

    @OperationLog(desc = "删除投诉类型信息")
    @RequestMapping(value = "/api/ComplaintTypes/{code}", method = RequestMethod.DELETE)
    //@Auth(allowedBizOp = {ServerConstantValue.GP_ADMIN ,ServerConstantValue.GP_CUSTOMER_SERVICE,ServerConstantValue.GP_GAS_STORE_LEADER})
    public ResponseEntity delete(@PathVariable("code") String code)
    {
        ResponseEntity responseEntity;

        complaintTypeService.deleteByCode(code);
        responseEntity = ResponseEntity.noContent().build();

        return responseEntity;
    }
}

package com.donno.nj.controller;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.constant.Constant;
import com.donno.nj.domain.SecurityType;
import com.donno.nj.representation.ListRep;
import com.donno.nj.service.SecurityTypeService;
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
public class SecurityTypeController
{

    @Autowired
    SecurityTypeService securityTypeService;

    @RequestMapping(value = "/api/SecurityTypes", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "获取安检类型列表")
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

        List<SecurityType> securityTypes = securityTypeService.retrieve(params);
        Integer count = securityTypeService.count(params);

        return ResponseEntity.ok(ListRep.assemble(securityTypes, count));
    }

    @OperationLog(desc = "创建安检类型")
    @RequestMapping(value = "/api/SecurityTypes", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody SecurityType securityType, UriComponentsBuilder ucBuilder)
    {
        ResponseEntity responseEntity;
        if (securityTypeService.findByCode(securityType.getCode()).isPresent())
        {
            responseEntity =  ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        else
        {
             /*创建*/
            securityTypeService.create(securityType);

            URI uri = ucBuilder.path("/api/securityTypes/{code}").buildAndExpand(securityType.getCode()).toUri();
            responseEntity = ResponseEntity.created(uri).build();
        }

        return responseEntity;
    }

    @OperationLog(desc = "修改安检类型信息")
    @RequestMapping(value = "/api/SecurityTypes/{code}", method = RequestMethod.PUT)
    public ResponseEntity update(@PathVariable("code") String code, @RequestBody SecurityType newsecurityType)
    {
        ResponseEntity responseEntity;

        securityTypeService.update(code, newsecurityType);
        responseEntity = ResponseEntity.ok().build();

        return responseEntity;
    }

    @OperationLog(desc = "删除安检类型信息")
    @RequestMapping(value = "/api/SecurityTypes/{code}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable("code") String code)
    {
        ResponseEntity responseEntity;

        securityTypeService.deleteByCode(code);
        responseEntity = ResponseEntity.noContent().build();

        return responseEntity;
    }
}

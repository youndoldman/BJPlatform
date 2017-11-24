package com.donno.nj.controller;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.constant.Constant;
import com.donno.nj.domain.SecurityCheckType;
import com.donno.nj.representation.ListRep;
import com.donno.nj.service.SecurityCheckTypeService;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static com.donno.nj.util.ParamMapBuilder.paginationParams;
import static com.google.common.collect.Maps.newHashMap;

@RestController
public class SecurityCheckTypeController
{

    @Autowired
    SecurityCheckTypeService securityCheckTypeService;

    @RequestMapping(value = "/api/securityCheckTypes", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "获取安全检查类型列表")
    public ResponseEntity retrieve(@RequestParam(value = "code", defaultValue = "") String code,
                                            @RequestParam(value = "orderBy", defaultValue = "") String orderBy,
                                            @RequestParam(value = "pageSize", defaultValue = Constant.PAGE_SIZE) Integer pageSize,
                                            @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo)
    {
        Map params = newHashMap(ImmutableMap.of("code", code));
        params.putAll(paginationParams(pageNo, pageSize, orderBy));

        List<SecurityCheckType> securityCheckTypes = securityCheckTypeService.retrieve(params);

        return ResponseEntity.ok(ListRep.assemble(securityCheckTypes, securityCheckTypes.size()));
    }

    @OperationLog(desc = "创建安全检查类型")
    @RequestMapping(value = "/api/securityCheckTypes", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody SecurityCheckType securityCheckType, UriComponentsBuilder ucBuilder)
    {

        ResponseEntity responseEntity;
        if (securityCheckTypeService.findByCode(securityCheckType.getCode()).isPresent())
        {
            responseEntity =  ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        else
        {
             /*创建*/
            securityCheckTypeService.create(securityCheckType);

            URI uri = ucBuilder.path("/api/securityCheckTypes/{code}").buildAndExpand(securityCheckType.getCode()).toUri();
            responseEntity = ResponseEntity.created(uri).build();
        }

        return responseEntity;
    }


    @OperationLog(desc = "修改安全检查类型信息")
    @RequestMapping(value = "/api/securityCheckTypes/{code}", method = RequestMethod.PUT)
    public ResponseEntity update(@PathVariable("code") String code, @RequestBody SecurityCheckType newSecurityCheckType)
    {
        ResponseEntity responseEntity;

        Optional<SecurityCheckType> securityCheckType = securityCheckTypeService.findByCode(code);
        if (securityCheckType.isPresent())
        {
            /*新的安全检查类型代码校验，不能已经存在*/
            if (securityCheckTypeService.findByCode(newSecurityCheckType.getCode()).isPresent())//目标code值已经存在
            {
                responseEntity =  ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            else
            {
                securityCheckTypeService.update(securityCheckType.get().getId(), newSecurityCheckType);
                responseEntity = ResponseEntity.ok().build();
            }
        }
        else
        {
            responseEntity = ResponseEntity.notFound().build();
        }

        return responseEntity;
    }

    @OperationLog(desc = "删除用户信息")
    @RequestMapping(value = "/api/securityCheckTypes/{code}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable("code") String code)
    {
        ResponseEntity responseEntity;

        Optional<SecurityCheckType> securityCheckType = securityCheckTypeService.findByCode(code);
        if (securityCheckType.isPresent())
        {
            securityCheckTypeService.deleteById(securityCheckType.get().getId());

            responseEntity = ResponseEntity.noContent().build();
        }
        else
        {
            responseEntity = ResponseEntity.notFound().build();
        }

        return responseEntity;
    }
}

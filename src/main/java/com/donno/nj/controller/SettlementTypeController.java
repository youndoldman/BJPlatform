package com.donno.nj.controller;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.constant.Constant;
import com.donno.nj.domain.CustomerType;
import com.donno.nj.domain.SettlementType;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.representation.ListRep;
import com.donno.nj.service.CustomerTypeService;
import com.donno.nj.service.SettlementTypeService;
import com.google.common.base.Optional;
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
public class SettlementTypeController
{

    @Autowired
    SettlementTypeService settlementTypeService ;


    @RequestMapping(value = "/api/SettlementType", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "获取结算类型信息列表")
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

        List<SettlementType> customerTypes = settlementTypeService.retrieve(params);
        Integer count = settlementTypeService.count(params);

        return ResponseEntity.ok(ListRep.assemble(customerTypes, count));
    }


    @OperationLog(desc = "创建结算类型信息")
    @RequestMapping(value = "/api/SettlementType", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody SettlementType settlementType, UriComponentsBuilder ucBuilder)
    {
        ResponseEntity responseEntity;

        /*创建*/
        settlementTypeService.create(settlementType);

        URI uri = ucBuilder.path("/api/SettlementType/{code}").buildAndExpand(settlementType.getCode()).toUri();
        responseEntity = ResponseEntity.created(uri).build();

        return responseEntity;
    }


    @OperationLog(desc = "修改用户结算类型信息")
    @RequestMapping(value = "/api/SettlementType/{code}", method = RequestMethod.PUT)
    public ResponseEntity update(@PathVariable("code")  String code,
                                 @RequestBody SettlementType newSettlementType)
    {
        ResponseEntity responseEntity;

        if (code == null || code.trim().length() == 0 )
        {
            throw new ServerSideBusinessException("请输入要删除的结算类型编码信息！",HttpStatus.NOT_ACCEPTABLE);
        }
        else
        {

            Optional<SettlementType> settlementTypeOptional = settlementTypeService.findByCode(code);
            if (settlementTypeOptional.isPresent())
            {
                settlementTypeService.update(settlementTypeOptional.get().getId(), newSettlementType);
                responseEntity = ResponseEntity.ok().build();
            }
            else
            {
                responseEntity = ResponseEntity.notFound().build();
            }
        }

        return responseEntity;
    }



    @OperationLog(desc = "删除结算类型信息")
    @RequestMapping(value = "/api/SettlementType/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable("code")  String code)
    {
        ResponseEntity responseEntity;

        Optional<SettlementType> settlementTypeOptional = settlementTypeService.findByCode(code);
        if (settlementTypeOptional.isPresent())
        {
            settlementTypeService.delete(settlementTypeOptional.get().getId());

            responseEntity = ResponseEntity.noContent().build();
        }
        else
        {
            responseEntity = ResponseEntity.notFound().build();
        }

        return responseEntity;
    }
}

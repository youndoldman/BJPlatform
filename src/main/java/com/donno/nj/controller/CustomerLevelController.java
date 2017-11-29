package com.donno.nj.controller;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.constant.Constant;
import com.donno.nj.domain.CustomerLevel;
import com.donno.nj.domain.CustomerType;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.representation.ListRep;
import com.donno.nj.service.CustomerLevelService;
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
public class CustomerLevelController
{

    @Autowired
    CustomerLevelService customerLevelService ;


    @RequestMapping(value = "/api/CustomerLevel", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "获取客户级别信息列表")
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

        List<CustomerLevel> customerLevels = customerLevelService.retrieve(params);

        return ResponseEntity.ok(ListRep.assemble(customerLevels, customerLevels.size()));
    }


    @OperationLog(desc = "创建客户级别信息")
    @RequestMapping(value = "/api/CustomerLevel", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody CustomerLevel customerLevel, UriComponentsBuilder ucBuilder)
    {
        ResponseEntity responseEntity;

        /*创建*/
        customerLevelService.create(customerLevel);

        URI uri = ucBuilder.path("/api/CustomerLevel/{code}").buildAndExpand(customerLevel.getCode()).toUri();
        responseEntity = ResponseEntity.created(uri).build();

        return responseEntity;
    }


    @OperationLog(desc = "修改用户级别信息")
    @RequestMapping(value = "/api/CustomerLevel", method = RequestMethod.PUT)
    public ResponseEntity update(@RequestParam(value = "code", defaultValue = "",required = true) String code,
                                 @RequestBody CustomerLevel newCustomerLevel)
    {
        ResponseEntity responseEntity;

        if (code == null || code.trim().length() == 0 )
        {
            throw new ServerSideBusinessException("请输入要删除的客户类型编码信息！",HttpStatus.NOT_ACCEPTABLE);
        }
        else
        {

            Optional<CustomerLevel> customerLevelOptional = customerLevelService.findByCode(code);
            if (customerLevelOptional.isPresent())
            {
                customerLevelService.update(customerLevelOptional.get().getId(), newCustomerLevel);
                responseEntity = ResponseEntity.ok().build();
            }
            else
            {
                responseEntity = ResponseEntity.notFound().build();
            }
        }

        return responseEntity;
    }



    @OperationLog(desc = "删除用户级别信息")
    @RequestMapping(value = "/api/CustomerLevel", method = RequestMethod.DELETE)
    public ResponseEntity delete(@RequestParam(value = "id", required = false) Integer id,
                                 @RequestParam(value = "code", defaultValue = "") String code,
                                 @RequestParam(value = "name", defaultValue = "") String name
                                )
    {
        ResponseEntity responseEntity;

        Map params = new HashMap<String,String>();

        if (id != null)
        {
            params.putAll(ImmutableMap.of("id", id));
        }

        if (code.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("code", code));
        }

        if (name.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("name", name));
        }

        customerLevelService.delete(params);
        responseEntity = ResponseEntity.noContent().build();

        return responseEntity;
    }
}

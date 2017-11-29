package com.donno.nj.controller;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.constant.Constant;
import com.donno.nj.domain.Customer;
import com.donno.nj.domain.CustomerType;
import com.donno.nj.domain.Group;
import com.donno.nj.domain.User;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.representation.ListRep;
import com.donno.nj.service.CustomerTypeService;
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
public class CustomerTypeController
{

    @Autowired
    CustomerTypeService customerTypeService ;


    @RequestMapping(value = "/api/CustomerType", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "获取客户类型信息列表")
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

        List<CustomerType> customerTypes = customerTypeService.retrieve(params);

        return ResponseEntity.ok(ListRep.assemble(customerTypes, customerTypes.size()));
    }


    @OperationLog(desc = "创建客户类型信息")
    @RequestMapping(value = "/api/CustomerType", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody CustomerType customerType, UriComponentsBuilder ucBuilder)
    {
        ResponseEntity responseEntity;

        /*创建*/
        customerTypeService.create(customerType);

        URI uri = ucBuilder.path("/api/CustomerType/{code}").buildAndExpand(customerType.getCode()).toUri();
        responseEntity = ResponseEntity.created(uri).build();

        return responseEntity;
    }


    @OperationLog(desc = "修改用户类型信息")
    @RequestMapping(value = "/api/CustomerType", method = RequestMethod.PUT)
    public ResponseEntity update(@RequestParam(value = "code", defaultValue = "",required = true) String code,
                                 @RequestBody CustomerType newCustomerType)
    {
        ResponseEntity responseEntity;

        if (code == null || code.trim().length() == 0 )
        {
            throw new ServerSideBusinessException("请输入要删除的客户类型编码信息！",HttpStatus.NOT_ACCEPTABLE);
        }
        else
        {

            Optional<CustomerType> customerType = customerTypeService.findByCode(code);
            if (customerType.isPresent())
            {
                customerTypeService.update(customerType.get().getId(), newCustomerType);
                responseEntity = ResponseEntity.ok().build();
            }
            else
            {
                responseEntity = ResponseEntity.notFound().build();
            }
        }

        return responseEntity;
    }



    @OperationLog(desc = "删除用户类型信息")
    @RequestMapping(value = "/api/CustomerType", method = RequestMethod.DELETE)
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

        customerTypeService.delete(params);
        responseEntity = ResponseEntity.noContent().build();

        return responseEntity;
    }
}

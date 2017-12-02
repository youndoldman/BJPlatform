package com.donno.nj.controller;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.constant.Constant;
import com.donno.nj.domain.CustomerSource;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.representation.ListRep;
import com.donno.nj.service.CustomerSourceService;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import com.google.common.base.Optional;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.donno.nj.util.ParamMapBuilder.paginationParams;

@RestController
public class CustomerSourceController
{

    @Autowired
    CustomerSourceService customerSourceService ;


    @RequestMapping(value = "/api/CustomerSource", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "获取客户来源信息列表")
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

        List<CustomerSource> customerTypes = customerSourceService.retrieve(params);
        Integer count = customerSourceService.count(params);

        return ResponseEntity.ok(ListRep.assemble(customerTypes, count));
    }


    @OperationLog(desc = "创建客户来源信息")
    @RequestMapping(value = "/api/CustomerSource", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody CustomerSource customerSource, UriComponentsBuilder ucBuilder)
    {
        ResponseEntity responseEntity;

        /*创建*/
        customerSourceService.create(customerSource);

        URI uri = ucBuilder.path("/api/CustomerSource/{code}").buildAndExpand(customerSource.getCode()).toUri();
        responseEntity = ResponseEntity.created(uri).build();

        return responseEntity;
    }


    @OperationLog(desc = "修改用户来源信息")
    @RequestMapping(value = "/api/CustomerSource", method = RequestMethod.PUT)
    public ResponseEntity update(@RequestParam(value = "code", defaultValue = "",required = true) String code,
                                 @RequestBody CustomerSource newCustomerSource)
    {
        ResponseEntity responseEntity;

        if (code == null || code.trim().length() == 0 )
        {
            throw new ServerSideBusinessException("请输入要删除的客户来源编码信息！",HttpStatus.NOT_ACCEPTABLE);
        }
        else
        {

            Optional<CustomerSource> customerSourceOptional = customerSourceService.findByCode(code);
            if (customerSourceOptional.isPresent())
            {
                customerSourceService.update(customerSourceOptional.get().getId(), newCustomerSource);
                responseEntity = ResponseEntity.ok().build();
            }
            else
            {
                responseEntity = ResponseEntity.notFound().build();
            }
        }

        return responseEntity;
    }



    @OperationLog(desc = "删除用户来源信息")
    @RequestMapping(value = "/api/CustomerSource", method = RequestMethod.DELETE)
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

        customerSourceService.delete(params);
        responseEntity = ResponseEntity.noContent().build();

        return responseEntity;
    }
}

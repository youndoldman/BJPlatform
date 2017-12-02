package com.donno.nj.controller;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.constant.Constant;
import com.donno.nj.domain.CustomerCallIn;
import com.donno.nj.representation.ListRep;
import com.donno.nj.service.*;
import com.donno.nj.util.AppUtil;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.donno.nj.util.ParamMapBuilder.paginationParams;

@RestController
public class CustomerCallInController
{

    @Autowired
    CustomerCallInService customerCallInService ;


    @RequestMapping(value = "/api/CustomerCallIn", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "获取客户呼入信息列表")
    public ResponseEntity retrieve(@RequestParam(value = "phone", defaultValue = "") String phone,
                                   @RequestParam(value = "userId", defaultValue = "") String userId,
                                            @RequestParam(value = "province", defaultValue = "") String province,
                                            @RequestParam(value = "city", defaultValue = "") String city,
                                            @RequestParam(value = "county", defaultValue = "") String county,
                                            @RequestParam(value = "detail", defaultValue = "") String detail,
                                            @RequestParam(value = "orderBy", defaultValue = "") String orderBy,
                                            @RequestParam(value = "pageSize", defaultValue = Constant.PAGE_SIZE) Integer pageSize,
                                            @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo)
    {
        Map params = new HashMap<String,String>();

        if (phone.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("phone", phone));
        }

        if (userId.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("userId", userId));
        }

        if (province.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("province", province));
        }

        if (city.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("city", city));
        }

        if (county.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("county", county));
        }

        if (detail.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("detail", detail));
        }

        params.putAll(paginationParams(pageNo, pageSize, orderBy));

        List<CustomerCallIn> customerCallIns = customerCallInService.retrieve(params);
        Integer count = customerCallInService.count(params);

        return ResponseEntity.ok(ListRep.assemble(customerCallIns, count));
    }

    @OperationLog(desc = "创建客户呼入信息")
    @RequestMapping(value = "/api/CustomerCallIn", method = RequestMethod.POST)
    public ResponseEntity create(@RequestParam(value = "userId", defaultValue = "") String userId,
                                @RequestBody CustomerCallIn customerCallIn, UriComponentsBuilder ucBuilder)
    {
        ResponseEntity responseEntity;

        /*创建*/
        customerCallInService.create(userId,customerCallIn);

        URI uri = ucBuilder.path("/api/CustomerCallIn/{phone}").buildAndExpand(customerCallIn.getPhone()).toUri();
        responseEntity = ResponseEntity.created(uri).build();

        return responseEntity;
    }



    @OperationLog(desc = "删除用户呼入信息")
    @RequestMapping(value = "/api/CustomerCallIn", method = RequestMethod.DELETE)
    public ResponseEntity delete(@RequestParam(value = "id",required=false) Integer id,
                                 @RequestParam(value = "userId", defaultValue = "" ) String userId,
                                 @RequestParam(value = "phone", defaultValue = "") String phone
                                )
    {
        ResponseEntity responseEntity;

        Map params = new HashMap<String,String>();

        if (id != null )
        {
            params.putAll(ImmutableMap.of("id", Integer.toString(id)));
        }

        if (phone.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("phone", phone));
        }

        if (userId.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("userId", userId));
        }

        customerCallInService.delete(params);
        responseEntity = ResponseEntity.noContent().build();

        return responseEntity;
    }
}

package com.donno.nj.controller;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.constant.Constant;
import com.donno.nj.domain.*;
import com.donno.nj.logger.DebugLogger;
import com.donno.nj.service.*;


import com.donno.nj.util.AppUtil;
import com.donno.nj.representation.ListRep;

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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.donno.nj.util.ParamMapBuilder.paginationParams;
import static com.google.common.collect.Maps.newHashMap;

@RestController
public class CustomerController
{
    @Autowired
    private GroupService groupService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    CustomerDistrictService customerDistrictService ;



    @RequestMapping(value = "/api/customers/findByUserId", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity findByUserId(@RequestParam(value = "userId", defaultValue = "") String userId)
    {
        ResponseEntity responseEntity;

        Optional<Customer> customerOptional = customerService.findByCstUserId(userId);
        if(!customerOptional.isPresent())
        {
            responseEntity =  ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        else
        {
            responseEntity = ResponseEntity.ok(customerOptional.get());
            AppUtil.setCurrentLoginUser(customerOptional.get());
        }

        return responseEntity;
    }


    @RequestMapping(value = "/api/customers/login", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity login(@RequestParam(value = "userId", defaultValue = "") String userId,
                                @RequestParam(value = "password", defaultValue = "") String password)
    {
        ResponseEntity responseEntity;

        Optional<User> validUser = customerService.findByUserId(userId);

        if (!validUser.isPresent())
        {
            responseEntity =  ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        else if (!password.equals(validUser.get().getPassword()))
        {
            responseEntity =  ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        else
        {
            Optional<Customer> customerOptional = customerService.findByCstUserId(userId);
            if(!customerOptional.isPresent())
            {
                responseEntity =  ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            else
            {
                responseEntity = ResponseEntity.ok(customerOptional.get());
                AppUtil.setCurrentLoginUser(customerOptional.get());
            }
//            Map params = new HashMap<String,String>();
//            params.putAll(ImmutableMap.of("userId", userId));
//            params.putAll(paginationParams(1, 1, ""));
//
//            List<Customer> customerList = customerService.retrieve(params);
//            if (customerList.size() > 0)
//            {
//                responseEntity = ResponseEntity.ok(customerList.get(0));
//                AppUtil.setCurrentLoginUser(customerList.get(0));
//            }
//            else
//            {
//                responseEntity =  ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//            }
        }

        return responseEntity;
    }

    @RequestMapping(value = "/api/customers/logout", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity logout(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        AppUtil.clearCurrentLoginUser();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @RequestMapping(value = "/api/customers", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "获取客户列表")
    public ResponseEntity retrieveCustomers(@RequestParam(value = "userId", defaultValue = "") String userId,
                                            @RequestParam(value = "userName", defaultValue = "") String userName,
                                            @RequestParam(value = "identity", defaultValue = "") String identity,
                                            @RequestParam(value = "number", defaultValue = "") String number,
                                            @RequestParam(value = "groupCode", defaultValue = "") String groupCode,
                                            @RequestParam(value = "status", defaultValue = "") String status,
                                            @RequestParam(value = "settlementTypeCode", defaultValue = "") String settlementTypeCode,
                                            @RequestParam(value = "customerTypeCode", defaultValue = "") String customerTypeCode,
                                            @RequestParam(value = "customerLevelCode", defaultValue = "") String customerLevelCode,
                                            @RequestParam(value = "customerSourceCode", defaultValue = "") String customerSourceCode,
                                            @RequestParam(value = "phone", defaultValue = "") String phone,
                                            @RequestParam(value = "companyName", defaultValue = "") String companyName,
                                            @RequestParam(value = "addrProvince", defaultValue = "") String addrProvince,
                                            @RequestParam(value = "addrCity", defaultValue = "") String addrCity,
                                            @RequestParam(value = "addrCounty", defaultValue = "") String addrCounty,
                                            @RequestParam(value = "addrDetail", defaultValue = "") String addrDetail,
                                            @RequestParam(value = "orderBy", defaultValue = "") String orderBy,
                                            @RequestParam(value = "pageSize", defaultValue = Constant.PAGE_SIZE) Integer pageSize,
                                            @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo)
    {
        String logInfo = String.format("start query :%s",new Date());
        DebugLogger.log(logInfo);

        Map params = new HashMap<String,String>();
        if (userId.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("userId", userId));
        }

        if (userName.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("userName", userName));
        }

        if (identity.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("identity", identity));
        }

        if (number.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("number", number));
        }

        if (groupCode.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("groupCode", groupCode));
        }

        if (status.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("status", status));
        }

        if (customerTypeCode.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("settlementTypeCode", settlementTypeCode));
        }

        if (customerTypeCode.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("customerTypeCode", customerTypeCode));
        }

        if (customerLevelCode.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("customerLevelCode", customerLevelCode));
        }

        if (customerSourceCode.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("customerSourceCode", customerSourceCode));
        }

        if (companyName.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("companyName", companyName));
        }

        if (phone.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("phone", phone));
        }

        if (addrProvince.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("addrProvince", addrProvince));
        }

        if (addrCity.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("addrCity", addrCity));
        }

        if (addrCounty.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("addrCounty", addrCounty));
        }

        if (addrDetail.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("addrDetail", addrDetail));
        }


        params.putAll(paginationParams(pageNo, pageSize, orderBy));

        List<Customer> customers = customerService.retrieve(params);

        Integer customerCount = customerService.count(params);

        logInfo = String.format("end query :%s",new Date());
        DebugLogger.log(logInfo);

        return ResponseEntity.ok(ListRep.assemble(customers, customerCount));
    }

    @OperationLog(desc = "创建客户")
    @RequestMapping(value = "/api/customers", method = RequestMethod.POST)
    public ResponseEntity createCustomer(@RequestBody Customer customer, UriComponentsBuilder ucBuilder)
    {
        ResponseEntity responseEntity;

        /*创建客户*/
        customerService.create(customer);

        URI uri = ucBuilder.path("/api/customers/{userId}").buildAndExpand(customer.getUserId()).toUri();
        responseEntity = ResponseEntity.created(uri).build();

        return responseEntity;
    }

//    @RequestMapping(value = "/api/customers/{number}", method = RequestMethod.GET)
//    public ResponseEntity<Customer> getCustomerByNumber(@PathVariable("number") String number)
//    {
//        ResponseEntity responseEntity;
//
////        Optional<Customer> curCustomer = customerService.findByNumber(number);
////        if (curCustomer.isPresent())
////        {
////            responseEntity = ResponseEntity.ok(curCustomer.get());
////        }
////        else
////        {
////            responseEntity = new ResponseEntity<Customer>(HttpStatus.NOT_FOUND);
////        }
//
//        return responseEntity;
//    }



    @OperationLog(desc = "修改用户信息")
    @RequestMapping(value = "/api/customers/{userId}", method = RequestMethod.PUT)
    public ResponseEntity updateCustomer(@PathVariable("userId") String userId, @RequestBody Customer newCustomer)
    {
        ResponseEntity responseEntity;

        Optional<User> user = customerService.findByUserId(userId);
        if (user.isPresent())
        {
            if (newCustomer.getUserGroup() != null)//是否含有组信息
            {
                Optional<Group> group = groupService.findByCode(newCustomer.getUserGroup().getCode());//查询组信息是否合法
                if (group.isPresent())
                {
                    newCustomer.setUserGroup(group.get());

                    customerService.update(user.get().getId(), newCustomer);
                    responseEntity = ResponseEntity.ok().build();
                }
                else
                {
                    responseEntity =  ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
                }
            }
            else
            {
                customerService.update(user.get().getId(), newCustomer);
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
    @RequestMapping(value = "/api/customers/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteCustomer(@PathVariable("userId") String userId)
    {
        ResponseEntity responseEntity;

        Optional<User> user = customerService.findByUserId(userId);
        if (user.isPresent())
        {
            customerService.deleteById(user.get().getId());

            responseEntity = ResponseEntity.noContent().build();
        }
        else
        {
            responseEntity = ResponseEntity.notFound().build();
        }

        return responseEntity;
    }
}

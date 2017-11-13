package com.donno.nj.controller;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.constant.Constant;
import com.donno.nj.domain.*;
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
    CustomerTypeService customerTypeService;

    @Autowired
    CustomerCharacterService customerCharacterService;

    @Autowired
    CustomerSourceService customerSourceService  ;

    @Autowired
    CustomerDistrictService customerDistrictService ;

    @Autowired
    GasStoreService gasStoreService ;

    @Autowired
    private CustomerAddressService customerAddressService;


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
            AppUtil.setCurrentLoginUser(validUser.get());
            responseEntity = ResponseEntity.ok(validUser.get());
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
                                            @RequestParam(value = "number", defaultValue = "") String number,
                                            @RequestParam(value = "name", defaultValue = "") String userName,
                                            @RequestParam(value = "phone1", defaultValue = "") String phone1,
                                            @RequestParam(value = "orderBy", defaultValue = "") String orderBy,
                                            @RequestParam(value = "pageSize", defaultValue = Constant.PAGE_SIZE) Integer pageSize,
                                            @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo)
    {


        Map params = newHashMap(ImmutableMap.of("name", userName));
        params.putAll(paginationParams(pageNo, pageSize, orderBy));

        List<Customer> customers = customerService.retrieve(params);

        return ResponseEntity.ok(ListRep.assemble(customers, customers.size()));
    }

    @OperationLog(desc = "创建客户")
    @RequestMapping(value = "/api/customers", method = RequestMethod.POST)
    public ResponseEntity createCustomer(@RequestBody Customer customer, UriComponentsBuilder ucBuilder)
    {

        ResponseEntity responseEntity;
        if (customerService.findByUserId(customer.getUserId()).isPresent())
        {
            responseEntity =  ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        else
        {
            /*自动生成客户编号NUMBER*/
            customer.setNumber(customer.getUserId());

             /*查询组对应的ID*/
            if (customer.getUserGroup() != null)
            {
                Optional<Group> group = groupService.findByCode(customer.getUserGroup().getCode());
                if(group.isPresent())
                {
                    customer.setUserGroup(group.get());
                    /*创建客户*/
                    customerService.create(customer);

                    URI uri = ucBuilder.path("/api/customers/{userId}").buildAndExpand(customer.getUserId()).toUri();
                    responseEntity = ResponseEntity.created(uri).build();
                }
                else
                {
                    responseEntity =  ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
                }
            }
            else
            {
                responseEntity =  ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
            }
        }

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

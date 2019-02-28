package com.donno.nj.controller;

import com.donno.nj.aspect.Auth;
import com.donno.nj.aspect.OperationLog;
import com.donno.nj.constant.Constant;
import com.donno.nj.domain.*;
import com.donno.nj.exception.ServerSideBusinessException;
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



    @Autowired
    private WeiXinPayService weiXinPayService;

    @RequestMapping(value = "/api/customers/findByUserId", method = RequestMethod.GET, produces = "application/json")
    //@Auth(allowedBizOp = {})
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

        Optional<User> validUser = customerService.findByUserIdPwd(userId,password);

        if (!validUser.isPresent())
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

        }

        return responseEntity;
    }


    @RequestMapping(value = "/api/customers/wx/login", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity login( @RequestParam(value = "userCode", defaultValue = "") String userCode)
            throws IOException
    {
        ResponseEntity responseEntity;

        String wxOpenId = weiXinPayService.getOpenId(userCode);
        if (wxOpenId == null || wxOpenId.trim().length() == 0)
        {
            throw new ServerSideBusinessException("OpenId获取失败！",HttpStatus.NOT_FOUND);
        }
        else
        {
            Optional<User> userOptional = customerService.findByWxOpenId(wxOpenId);

            if (userOptional.isPresent())
            {
                Optional<Customer> customerOptional = customerService.findByCstUserId(userOptional.get().getUserId());
                if(!customerOptional.isPresent())
                {
                    throw new ServerSideBusinessException("客户信息错误！",HttpStatus.UNAUTHORIZED);
                }
                else
                {
                    responseEntity = ResponseEntity.ok(customerOptional.get());
                    AppUtil.setCurrentLoginUser(customerOptional.get());
                }
            }
            else
            {
                throw new ServerSideBusinessException("客户未绑定微信！",HttpStatus.UNAUTHORIZED);
            }
        }

        return responseEntity;
    }


    @RequestMapping(value = "/api/customers/wx/bind", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity bindWxOpenId(@RequestParam(value = "userId", defaultValue = "") String userId,
                                       @RequestParam(value = "password", defaultValue = "") String password ,
                                        @RequestParam(value = "userCode", defaultValue = "") String userCode)
            throws IOException
    {
        ResponseEntity responseEntity;

        String wxOpenId = weiXinPayService.getOpenId(userCode);
        if (wxOpenId == null || wxOpenId.trim().length() == 0)
        {
            throw new ServerSideBusinessException("OpenId获取失败！",HttpStatus.NOT_FOUND);
        }
        else
        {
            //String wxOpenId = "123456789";
            Optional<User> userOptional = customerService.findByWxOpenId(wxOpenId);

            if (userOptional.isPresent())
            {
                throw new ServerSideBusinessException("客户已经绑定微信！",HttpStatus.NOT_ACCEPTABLE);
            }
            else
            {
                Optional<User> validUser = customerService.findByUserIdPwd(userId,password);
                if(!validUser.isPresent())
                {
                    throw new ServerSideBusinessException("客户名称或密码错误！",HttpStatus.UNAUTHORIZED);
                }
                else
                {
                    customerService.bindWxOpenId(userId,wxOpenId);
                    responseEntity = ResponseEntity.ok().build();
                }
            }
        }

        return responseEntity;
    }

    @RequestMapping(value = "/api/customers/wx/unBind", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity unBindWxOpenId(@RequestParam(value = "userId", defaultValue = "") String userId,
                                       @RequestParam(value = "password", defaultValue = "") String password ,
                                       @RequestParam(value = "userCode", defaultValue = "") String userCode)
            throws IOException
    {
        ResponseEntity responseEntity;

        String wxOpenId = weiXinPayService.getOpenId(userCode);
        if (wxOpenId == null || wxOpenId.trim().length() == 0)
        {
            throw new ServerSideBusinessException("OpenId获取失败！",HttpStatus.NOT_FOUND);
        }
        else
        {
            //String wxOpenId = "123456789";

            Optional<User> userOptional = customerService.findByWxOpenId(wxOpenId);

            if (!userOptional.isPresent())
            {
                throw new ServerSideBusinessException("客户未绑定微信！",HttpStatus.NOT_ACCEPTABLE);
            }
            else
            {
                Optional<User> validUser = customerService.findByUserIdPwd(userId,password);
                if(!validUser.isPresent())
                {
                    throw new ServerSideBusinessException("客户名称或密码错误！",HttpStatus.UNAUTHORIZED);
                }
                else
                {
                    customerService.unBindWxOpenId(userId,wxOpenId);
                    responseEntity = ResponseEntity.ok().build();
                }
            }
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
    //@Auth(allowedBizOp = {})
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
                                            @RequestParam(value = "sleepDays", required = false) Integer sleepDays,
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

        if (sleepDays != null)
        {
            params.putAll(ImmutableMap.of("sleepDays", sleepDays));
        }

        if (settlementTypeCode.trim().length() > 0)
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
    //@Auth(allowedBizOp = {ServerConstantValue.GP_ADMIN,ServerConstantValue.GP_CUSTOMER_SERVICE})
    public ResponseEntity createCustomer(@RequestBody Customer customer, UriComponentsBuilder ucBuilder)
    {
        ResponseEntity responseEntity;

        /*创建客户*/
        customerService.create(customer);

        URI uri = ucBuilder.path("/api/customers/{userId}").buildAndExpand(customer.getUserId()).toUri();
        responseEntity = ResponseEntity.created(uri).build();

        return responseEntity;
    }


    @OperationLog(desc = "修改用户信息")
    @RequestMapping(value = "/api/customers/{userId}", method = RequestMethod.PUT)
    //@Auth(allowedBizOp = {ServerConstantValue.GP_ADMIN,ServerConstantValue.GP_CUSTOMER_SERVICE})
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
    //@Auth(allowedBizOp = {ServerConstantValue.GP_ADMIN,ServerConstantValue.GP_CUSTOMER_SERVICE})
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



    @OperationLog(desc = "增加推荐人")
    @RequestMapping(value = "/api/customers/referee", method = RequestMethod.POST)
    public void addReferee(@RequestParam("customerId") String customerId,@RequestParam(value = "refereeId", defaultValue = "") String refereeId)
    {
        /*参数校验*/
        if (customerId == null || customerId.trim().length() == 0 )
        {
            throw new ServerSideBusinessException("客户信息不全，缺少客户ID！", HttpStatus.NOT_ACCEPTABLE);
        }

        if (refereeId == null || refereeId.trim().length() == 0 )
        {
            throw new ServerSideBusinessException("推荐人信息不全，缺少推荐人ID！", HttpStatus.NOT_ACCEPTABLE);
        }

        customerService.addReferee(customerId,refereeId);
    }


    @OperationLog(desc = "解除推荐人")
    @RequestMapping(value = "/api/customers/referee", method = RequestMethod.DELETE)
    public void removeReferee(@RequestParam("customerId") String customerId,@RequestParam(value = "refereeId", defaultValue = "") String refereeId)
    {
        /*参数校验*/
        if (customerId == null || customerId.trim().length() == 0 )
        {
            throw new ServerSideBusinessException("客户信息不全，缺少客户ID！", HttpStatus.NOT_ACCEPTABLE);
        }

        if (refereeId == null || refereeId.trim().length() == 0 )
        {
            throw new ServerSideBusinessException("推荐人信息不全，缺少推荐人ID！", HttpStatus.NOT_ACCEPTABLE);
        }

        customerService.removeReferee(customerId,refereeId);
    }

    @OperationLog(desc = "查询推荐人")
    @RequestMapping(value = "/api/customers/referee", method = RequestMethod.GET)
    public ResponseEntity getReferee(@RequestParam("customerId") String customerId)
    {
        /*参数校验*/
        if (customerId == null || customerId.trim().length() == 0 )
        {
            throw new ServerSideBusinessException("客户信息不全，缺少客户ID！", HttpStatus.NOT_ACCEPTABLE);
        }

        List<CstRefereeRel> CstRefereeRels = customerService.getReferee(customerId);

        return ResponseEntity.ok(ListRep.assemble(CstRefereeRels, CstRefereeRels.size()));
    }
}

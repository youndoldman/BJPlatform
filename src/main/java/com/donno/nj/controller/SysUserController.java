package com.donno.nj.controller;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.constant.Constant;
import com.donno.nj.domain.Group;
import com.donno.nj.domain.SysUser;
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
import java.util.List;
import java.util.Map;

import static com.donno.nj.util.ParamMapBuilder.paginationParams;
import static com.google.common.collect.Maps.newHashMap;

@RestController
public class SysUserController
{
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private GroupService groupService;


    @RequestMapping(value = "/api/sysusers/login", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity login(@RequestParam(value = "userId", defaultValue = "") String userId,
                                @RequestParam(value = "password", defaultValue = "") String password)
    {
        ResponseEntity responseEntity;

        Optional<SysUser> validUser = sysUserService.findByUserId(userId);

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

    @RequestMapping(value = "/api/sysusers/logout", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity logout(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        AppUtil.clearCurrentLoginUser();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @RequestMapping(value = "/api/sysusers", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "获取客户列表")
    public ResponseEntity retrieve(@RequestParam(value = "userId", defaultValue = "") String userId,
                                            @RequestParam(value = "jobNumber", defaultValue = "") String jobNumber,
                                            @RequestParam(value = "name", defaultValue = "") String userName,
                                            @RequestParam(value = "mobilePhone", defaultValue = "") String mobilePhone,
                                            @RequestParam(value = "officePhone", defaultValue = "") String officePhone,
                                            @RequestParam(value = "orderBy", defaultValue = "userId") String orderBy,
                                            @RequestParam(value = "pageSize", defaultValue = Constant.PAGE_SIZE) Integer pageSize,
                                            @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo)
    {
        Map params = newHashMap(ImmutableMap.of("userId", userId));
        params.putAll(paginationParams(pageNo, pageSize, orderBy));

        List<SysUser> users = sysUserService.retrieve(params);
        Integer total = sysUserService.count(params);

        return ResponseEntity.ok(ListRep.assemble(users, total));
    }

    @OperationLog(desc = "创建客户")
    @RequestMapping(value = "/api/sysusers", method = RequestMethod.POST)
    public ResponseEntity createCustomer(@RequestBody SysUser sysUser, UriComponentsBuilder ucBuilder)
    {
        ResponseEntity responseEntity;
        if (sysUserService.findByUserId(sysUser.getUserId()).isPresent())
        {
            responseEntity =  ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        else
        {
            /*查询组对应的ID*/
            Optional<Group> group = groupService.findByCode(sysUser.getGroup().getCode());
            if(group.isPresent())
            {
                sysUser.setGroup(group.get());
                SysUser newUser = sysUserService.create(sysUser);
                URI uri = ucBuilder.path("/api/sysusers/{userId}").buildAndExpand(newUser.getUserId()).toUri();
                responseEntity = ResponseEntity.created(uri).build();
            }
            else //异常数据，返回错误信息
            {
                responseEntity =  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

            }
        }
        return responseEntity;
    }


    @OperationLog(desc = "修改用户信息")
    @RequestMapping(value = "/api/sysusers/{userId}", method = RequestMethod.PUT)
    public ResponseEntity updateCustomer(@PathVariable("userId") String userId, @RequestBody SysUser newUser)
    {
        ResponseEntity responseEntity;

        Optional<SysUser> curUser = sysUserService.findByUserId(userId);
        if (curUser.isPresent())
        {
            sysUserService.update(curUser.get(), newUser);
            responseEntity = ResponseEntity.ok().build();
        }
        else
        {
            responseEntity = ResponseEntity.notFound().build();
        }

        return responseEntity;
    }

    @OperationLog(desc = "删除用户信息")
    @RequestMapping(value = "/api/sysusers/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteCustomer(@PathVariable("userId") String userId)
    {
        ResponseEntity responseEntity;

        Optional<SysUser> curUser = sysUserService.findByUserId(userId);
        if (curUser.isPresent())
        {
            sysUserService.delete(curUser.get().getUserId());
            responseEntity = ResponseEntity.noContent().build();
        }
        else
        {
            responseEntity = ResponseEntity.notFound().build();
        }

        return responseEntity;
    }
}

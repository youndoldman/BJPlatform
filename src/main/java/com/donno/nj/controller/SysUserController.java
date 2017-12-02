package com.donno.nj.controller;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.constant.Constant;
import com.donno.nj.domain.UserPosition;
import com.donno.nj.domain.SysUser;
import com.donno.nj.domain.User;
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

        Optional<User> validUser = sysUserService.findByUserId(userId);

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
            Map params = new HashMap<String,String>();
            params.putAll(ImmutableMap.of("userId", userId));
            params.putAll(paginationParams(1, 1, ""));

            List<SysUser> sysUserList = sysUserService.retrieve(params);
            if (sysUserList.size() > 0)
            {
                AppUtil.setCurrentLoginUser(validUser.get());
                responseEntity = ResponseEntity.ok(sysUserList.get(0));
            }
            else
            {
                responseEntity =  ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
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
                                   @RequestParam(value = "userName", defaultValue = "") String userName,
                                   @RequestParam(value = "jobNumber", defaultValue = "") String jobNumber,
                                   @RequestParam(value = "identity", defaultValue = "") String identity,
                                   @RequestParam(value = "groupCode", defaultValue = "") Integer groupCode,
                                   @RequestParam(value = "departmentCode", defaultValue = "") Integer departmentCode,
                                   @RequestParam(value = "mobilePhone", defaultValue = "") String mobilePhone,
                                   @RequestParam(value = "officePhone", defaultValue = "") String officePhone,
                                   @RequestParam(value = "orderBy", defaultValue = "") String orderBy,
                                   @RequestParam(value = "pageSize", defaultValue = Constant.PAGE_SIZE) Integer pageSize,
                                   @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo)
    {
        Map params = new HashMap<String,String>();
        if (userId.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("userId", userId));
        }

        if (userName.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("userName", userName));
        }

        if (jobNumber.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("jobNumber", jobNumber));
        }

        if (identity.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("identity", identity));
        }

        if (departmentCode != null)
        {
            params.putAll(ImmutableMap.of("departmentCode", departmentCode));
        }

        if (groupCode != null)
        {
            params.putAll(ImmutableMap.of("groupCode", groupCode));
        }

        if (mobilePhone.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("mobilePhone", mobilePhone));
        }

        if (officePhone.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("officePhone", officePhone));
        }

        params.putAll(paginationParams(pageNo, pageSize, orderBy));

        List<SysUser> users = sysUserService.retrieve(params);
        Integer count = sysUserService.count(params);

        return ResponseEntity.ok(ListRep.assemble(users, count));
    }

    @OperationLog(desc = "创建用户")
    @RequestMapping(value = "/api/sysusers", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody SysUser sysUser, UriComponentsBuilder ucBuilder)
    {
        ResponseEntity responseEntity;

        sysUserService.create(sysUser);
        URI uri = ucBuilder.path("/api/sysusers/{userId}").buildAndExpand(sysUser.getUserId()).toUri();
        responseEntity = ResponseEntity.created(uri).build();

        return responseEntity;
    }


    @OperationLog(desc = "修改用户信息")
    @RequestMapping(value = "/api/sysusers", method = RequestMethod.PUT)
    public ResponseEntity update(@RequestParam(value = "userId", defaultValue = "",required = true) String userId, @RequestBody SysUser newUser)
    {
        ResponseEntity responseEntity;

        Optional<User> user = sysUserService.findByUserId(userId);
        if (user.isPresent())
        {
            sysUserService.update(user.get().getId(), newUser);
            responseEntity = ResponseEntity.ok().build();
        }
        else
        {
            responseEntity = ResponseEntity.notFound().build();
        }

        return responseEntity;
    }

    @OperationLog(desc = "删除用户信息")
    @RequestMapping(value = "/api/sysusers", method = RequestMethod.DELETE)
    public ResponseEntity delete(
            //@RequestParam(value = "id", required = false) Integer id,
                                         @RequestParam(value = "userId", defaultValue = "") String userId
//                                         @RequestParam(value = "userName", defaultValue = "") String userName,
//                                         @RequestParam(value = "jobNumber", defaultValue = "") String jobNumber,
//                                         @RequestParam(value = "identity", defaultValue = "") String identity,
//                                         @RequestParam(value = "groupIdx", required = false) Integer groupIdx,
//                                         @RequestParam(value = "departmentIdx", required = false) Integer departmentIdx
                                         )
    {
        ResponseEntity responseEntity;

        Optional<User> user = sysUserService.findByUserId(userId);
        if (user.isPresent())
        {
            sysUserService.delete(user.get().getId());
            responseEntity = ResponseEntity.noContent().build();
        }
        else
        {
            responseEntity = ResponseEntity.notFound().build();
        }

        return responseEntity;
    }


    @OperationLog(desc = "更新位置信息")
    @RequestMapping(value = "/api/sysusers/position", method = RequestMethod.POST)
    public ResponseEntity updatePostion(@RequestParam(value = "userId", defaultValue = "",required = true) String userId,
                                        @RequestBody UserPosition userPosition )
    {
        ResponseEntity responseEntity;
        sysUserService.updatePosition(userId,userPosition);
        responseEntity = ResponseEntity.ok().build();

        return responseEntity;
    }


}

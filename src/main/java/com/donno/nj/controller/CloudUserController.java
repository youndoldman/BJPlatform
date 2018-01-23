package com.donno.nj.controller;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.constant.Constant;
import com.donno.nj.dao.CloudPanvaUserBindRelationDao;
import com.donno.nj.domain.*;
import com.donno.nj.representation.ListRep;
import com.donno.nj.service.CloudUserService;
import com.donno.nj.service.SysUserService;
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
import java.util.*;

import static com.donno.nj.util.ParamMapBuilder.paginationParams;

@RestController
public class CloudUserController
{
    @Autowired
    private CloudUserService cloudUserService;


    public CloudUserController()
    {

    }


    @RequestMapping(value = "/api/CloudUser", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "获取客户列表")
    public ResponseEntity retrieve(@RequestParam(value = "cloudUserId", defaultValue = "") String cloudUserId,
                                   @RequestParam(value = "panvaUserId", defaultValue = "") String panvaUserId,
                                   @RequestParam(value = "orderBy", defaultValue = "") String orderBy,
                                   @RequestParam(value = "pageSize", defaultValue = Constant.PAGE_SIZE) Integer pageSize,
                                   @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo)
    {
        Map params = new HashMap<String,String>();
        if (cloudUserId.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("cloudUserId", cloudUserId));
        }

        if (panvaUserId.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("panvaUserId", panvaUserId));
        }


        params.putAll(paginationParams(pageNo, pageSize, orderBy));

        List<CloudUser> cloudUsers = cloudUserService.retrieve(params);
        Integer count = cloudUserService.count(params);

        return ResponseEntity.ok(ListRep.assemble(cloudUsers, count));
    }

    @OperationLog(desc = "创建云客服用户")
    @RequestMapping(value = "/api/CloudUser", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody CloudUser cloudUser, UriComponentsBuilder ucBuilder)
    {
        ResponseEntity responseEntity;

        cloudUserService.create(cloudUser);
        URI uri = ucBuilder.path("/api/CloudUser/{userId}").buildAndExpand(cloudUser.getUserId()).toUri();
        responseEntity = ResponseEntity.created(uri).build();

        return responseEntity;
    }


    @OperationLog(desc = "修改云客服用户信息")
    @RequestMapping(value = "/api/CloudUser", method = RequestMethod.PUT)
    public ResponseEntity update(@RequestParam(value = "userId", defaultValue = "") String userId,
                                 @RequestBody CloudUser newUser)
    {
        ResponseEntity responseEntity;

        Optional<CloudUser> user = cloudUserService.findByCloudUserId(userId);
        if (user.isPresent())
        {
            cloudUserService.update(user.get().getUserId(), newUser);
            responseEntity = ResponseEntity.ok().build();
        }
        else
        {
            responseEntity = ResponseEntity.notFound().build();
        }

        return responseEntity;
    }

    @OperationLog(desc = "删除云用户信息")
    @RequestMapping(value = "/api/CloudUser", method = RequestMethod.DELETE)
    public ResponseEntity delete(@RequestParam(value = "userId", defaultValue = "") String userId)
    {
        ResponseEntity responseEntity;

        Optional<CloudUser> user = cloudUserService.findByCloudUserId(userId);
        if (user.isPresent())
        {
            cloudUserService.delete(user.get().getId());
            responseEntity = ResponseEntity.noContent().build();
        }
        else
        {
            responseEntity = ResponseEntity.notFound().build();
        }

        return responseEntity;
    }



    @OperationLog(desc = "云客服绑定系统用户")
    @RequestMapping(value = "/api/CloudUser/Bind", method = RequestMethod.PUT)
    public ResponseEntity bindLocationDev(@RequestParam(value = "userId", defaultValue = "") String userId,
                                          @RequestParam(value = "panvaUserId", defaultValue = "") String panvaUserId)
    {
        ResponseEntity responseEntity;

        cloudUserService.bindPanvaUser(userId,panvaUserId);
        responseEntity = ResponseEntity.ok().build();

        return responseEntity;
    }


    @OperationLog(desc = "解除绑定系统用户")
    @RequestMapping(value = "/api/CloudUser/UnBind", method = RequestMethod.PUT)
    public ResponseEntity unBindLocationDev(@RequestParam(value = "userId", defaultValue = "") String userId,
                                            @RequestParam(value = "panvaUserId", defaultValue = "") String panvaUserId)
    {
        ResponseEntity responseEntity;
        cloudUserService.unBindPanvaUser(userId,panvaUserId);
        responseEntity = ResponseEntity.ok().build();

        return responseEntity;
    }




}

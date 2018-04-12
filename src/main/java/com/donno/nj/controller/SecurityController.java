package com.donno.nj.controller;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.constant.Constant;
import com.donno.nj.domain.EProcessStatus;
import com.donno.nj.domain.Security;
import com.donno.nj.representation.ListRep;
import com.donno.nj.service.SecurityService;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.donno.nj.util.ParamMapBuilder.paginationParams;

@RestController
public class SecurityController
{
    @Autowired
    private SecurityService securityService;

    @RequestMapping(value = "/api/Security", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "获取安检订单列表")
    public ResponseEntity retrieve(@RequestParam(value = "securitySn", defaultValue = "") String securitySn,
                                   @RequestParam(value = "processStatus", defaultValue = "") EProcessStatus processStatus,
                                   @RequestParam(value = "securityTypeCode", defaultValue = "") String securityTypeCode,
                                   @RequestParam(value = "dealedUserId", defaultValue = "") String dealedUserId,
                                   @RequestParam(value = "requestCustomerId", defaultValue = "") String requestCustomerId,
                                   @RequestParam(value = "liableDepartmentCode", defaultValue = "") String liableDepartmentCode,
                                   @RequestParam(value = "startTime", defaultValue = "") String startTime,
                                   @RequestParam(value = "endTime", defaultValue = "") String endTime,
                                   @RequestParam(value = "orderBy", defaultValue = "") String orderBy,
                                   @RequestParam(value = "pageSize", defaultValue = Constant.PAGE_SIZE) Integer pageSize,
                                   @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo)
    {
        Map params = new HashMap<String,String>();

        if (securitySn.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("securitySn", securitySn));
        }

        if (processStatus != null)
        {
            params.putAll(ImmutableMap.of("processStatus", processStatus));
        }

        if (securityTypeCode.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("securityTypeCode", securityTypeCode));
        }

        if (dealedUserId.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("dealedUserId", dealedUserId));
        }

        if (requestCustomerId.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("requestCustomerId", requestCustomerId));
        }

        if (liableDepartmentCode.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("liableDepartmentCode", liableDepartmentCode));
        }

        if (startTime.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("startTime", startTime));
        }
        if (endTime.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("endTime", endTime));
        }

        params.putAll(paginationParams(pageNo, pageSize, orderBy));

        List<Security> securitys = securityService.retrieve(params);
        Integer count = securityService.count(params);
        return ResponseEntity.ok(ListRep.assemble(securitys, count));
    }

    @OperationLog(desc = "创建安检订单")
    @RequestMapping(value = "/api/Security", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody Security security, UriComponentsBuilder ucBuilder)
    {
        ResponseEntity responseEntity;

        securityService.create(security);

        URI uri = ucBuilder.path("/api/security/{securitySn}").buildAndExpand(security.getsecuritySn()).toUri();
        responseEntity = ResponseEntity.created(uri).build();

        return responseEntity;
    }

    @OperationLog(desc = "修改安检订单")
    @RequestMapping(value = "/api/Security/{securitySn}", method = RequestMethod.PUT)
    public ResponseEntity update(@PathVariable("securitySn") String securitySn, @RequestBody Security newsecurity)
    {
        ResponseEntity responseEntity;

        securityService.update(securitySn,newsecurity);
        responseEntity = ResponseEntity.ok().build();

        return responseEntity;
    }

    @OperationLog(desc = "删除安检订单")
    @RequestMapping(value = "/api/Security/{securitySn}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable("securitySn") String securitySn)
    {
        ResponseEntity responseEntity;

        Optional<Security> security = securityService.findBySn(securitySn);
        if (security.isPresent())
        {
            securityService.deleteById(security.get().getId());
            responseEntity = ResponseEntity.noContent().build();
        }
        else
        {
            responseEntity = ResponseEntity.notFound().build();
        }

        return responseEntity;
    }
}

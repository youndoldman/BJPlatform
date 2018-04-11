package com.donno.nj.controller;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.constant.Constant;
import com.donno.nj.domain.Mend;
import com.donno.nj.domain.EProcessStatus;
import com.donno.nj.representation.ListRep;
import com.donno.nj.service.MendService;
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
public class MendController
{
    @Autowired
    private MendService mendService;

    @RequestMapping(value = "/api/Mend", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "获取报修订单列表")
    public ResponseEntity retrieve(@RequestParam(value = "mendSn", defaultValue = "") String mendSn,
                                   @RequestParam(value = "processStatus", defaultValue = "") EProcessStatus processStatus,
                                   @RequestParam(value = "mendTypeCode", defaultValue = "") String mendTypeCode,
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

        if (mendSn.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("mendSn", mendSn));
        }

        if (processStatus != null)
        {
            params.putAll(ImmutableMap.of("processStatus", processStatus));
        }

        if (mendTypeCode.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("mendTypeCode", mendTypeCode));
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
            params.putAll(ImmutableMap.of("endTime", liableDepartmentCode));
        }

        params.putAll(paginationParams(pageNo, pageSize, orderBy));

        List<Mend> mends = mendService.retrieve(params);
        Integer count = mendService.count(params);
        return ResponseEntity.ok(ListRep.assemble(mends, count));
    }

    @OperationLog(desc = "创建报修订单")
    @RequestMapping(value = "/api/Mend", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody Mend mend, UriComponentsBuilder ucBuilder)
    {
        ResponseEntity responseEntity;

        mendService.create(mend);

        URI uri = ucBuilder.path("/api/Mend/{mendSn}").buildAndExpand(mend.getMendSn()).toUri();
        responseEntity = ResponseEntity.created(uri).build();

        return responseEntity;
    }

    @OperationLog(desc = "修改报修订单")
    @RequestMapping(value = "/api/Mend/{mendSn}", method = RequestMethod.PUT)
    public ResponseEntity update(@PathVariable("mendSn") String mendSn, @RequestBody Mend newMend)
    {
        ResponseEntity responseEntity;

        mendService.update(mendSn,newMend);
        responseEntity = ResponseEntity.ok().build();

        return responseEntity;
    }

    @OperationLog(desc = "删除报修订单")
    @RequestMapping(value = "/api/Mend/{mendSn}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable("mendSn") String mendSn)
    {
        ResponseEntity responseEntity;

        Optional<Mend> mend = mendService.findBySn(mendSn);
        if (mend.isPresent())
        {
            mendService.deleteById(mend.get().getId());
            responseEntity = ResponseEntity.noContent().build();
        }
        else
        {
            responseEntity = ResponseEntity.notFound().build();
        }

        return responseEntity;
    }
}

package com.donno.nj.controller;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.constant.Constant;
import com.donno.nj.domain.EProcessStatus;
import com.donno.nj.domain.Complaint;
import com.donno.nj.representation.ListRep;
import com.donno.nj.service.ComplaintService;
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
public class ComplaintController
{
    @Autowired
    private ComplaintService complaintService;

    @RequestMapping(value = "/api/Complaint", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "获取投诉订单列表")
    public ResponseEntity retrieve(@RequestParam(value = "complaintSn", defaultValue = "") String complaintSn,
                                   @RequestParam(value = "processStatus", defaultValue = "") EProcessStatus processStatus,
                                   @RequestParam(value = "complaintTypeCode", defaultValue = "") String complaintTypeCode,
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

        if (complaintSn.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("complaintSn", complaintSn));
        }

        if (processStatus != null)
        {
            params.putAll(ImmutableMap.of("processStatus", processStatus));
        }

        if (complaintTypeCode.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("complaintTypeCode", complaintTypeCode));
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

        List<Complaint> complaints = complaintService.retrieve(params);
        Integer count = complaintService.count(params);
        return ResponseEntity.ok(ListRep.assemble(complaints, count));
    }

    @OperationLog(desc = "创建投诉订单")
    @RequestMapping(value = "/api/Complaint", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody Complaint complaint, UriComponentsBuilder ucBuilder)
    {
        ResponseEntity responseEntity;

        complaintService.create(complaint);

        URI uri = ucBuilder.path("/api/complaint/{complaintSn}").buildAndExpand(complaint.getcomplaintSn()).toUri();
        responseEntity = ResponseEntity.created(uri).build();

        return responseEntity;
    }

    @OperationLog(desc = "修改投诉订单")
    @RequestMapping(value = "/api/Complaint/{complaintSn}", method = RequestMethod.PUT)
    public ResponseEntity update(@PathVariable("complaintSn") String complaintSn, @RequestBody Complaint newcomplaint)
    {
        ResponseEntity responseEntity;

        complaintService.update(complaintSn,newcomplaint);
        responseEntity = ResponseEntity.ok().build();

        return responseEntity;
    }

    @OperationLog(desc = "删除投诉订单")
    @RequestMapping(value = "/api/Complaint/{complaintSn}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable("complaintSn") String complaintSn)
    {
        ResponseEntity responseEntity;

        Optional<Complaint> complaint = complaintService.findBySn(complaintSn);
        if (complaint.isPresent())
        {
            complaintService.deleteById(complaint.get().getId());
            responseEntity = ResponseEntity.noContent().build();
        }
        else
        {
            responseEntity = ResponseEntity.notFound().build();
        }

        return responseEntity;
    }
}

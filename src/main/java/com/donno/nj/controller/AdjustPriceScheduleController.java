package com.donno.nj.controller;

import com.donno.nj.aspect.Auth;
import com.donno.nj.aspect.OperationLog;
import com.donno.nj.constant.Constant;
import com.donno.nj.domain.*;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.representation.ListRep;
import com.donno.nj.service.*;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.donno.nj.util.ParamMapBuilder.paginationParams;

@RestController
public class AdjustPriceScheduleController
{
    @Autowired
    private AdjustPriceScheduleService adjustPriceScheduleService;

    @RequestMapping(value = "/api/AdjustPriceSchedules", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "获取调价计划列表")
    //@Auth(allowedBizOp = {})
    public ResponseEntity retrieve(@RequestParam(value = "name", defaultValue = "") String name,
                                   @RequestParam(value = "status", required = false) Integer status,
                                   @RequestParam(value = "startTime", defaultValue = "") String startTime,
                                   @RequestParam(value = "endTime", defaultValue = "") String endTime,
                                   @RequestParam(value = "orderBy", defaultValue = "") String orderBy,
                                   @RequestParam(value = "pageSize", defaultValue = Constant.PAGE_SIZE) Integer pageSize,
                                   @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo)
    {
        Map params = new HashMap<String,String>();
        if (name.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("name", name));
        }

        if (status != null)
        {
            params.putAll(ImmutableMap.of("status", status));
        }


        if ( startTime != null && startTime.trim().length() > 0 )
        {
            params.putAll(ImmutableMap.of("startTime", startTime));
        }

        if ( endTime != null && endTime.trim().length() > 0 )
        {
            params.putAll(ImmutableMap.of("endTime", endTime));
        }


        params.putAll(paginationParams(pageNo, pageSize, orderBy));

        List<AdjustPriceSchedule> schedules = adjustPriceScheduleService.retrieve(params);
        Integer count = adjustPriceScheduleService.count(params);
        return ResponseEntity.ok(ListRep.assemble(schedules, count));
    }

    @OperationLog(desc = "创建调价计划")
    @RequestMapping(value = "/api/AdjustPriceSchedules", method = RequestMethod.POST)
    @Auth(allowedBizOp = {ServerConstantValue.GP_ADMIN })
    public ResponseEntity create(@RequestBody AdjustPriceSchedule adjustPriceSchedule, UriComponentsBuilder ucBuilder)
    {
        ResponseEntity responseEntity;

        /*创建订单入库*/
        adjustPriceScheduleService.create(adjustPriceSchedule);

        URI uri = ucBuilder.path("/api/AdjustPriceSchedules/{id}").buildAndExpand(adjustPriceSchedule.getId()).toUri();
        responseEntity = ResponseEntity.created(uri).build();

        return responseEntity;
    }

    @OperationLog(desc = "修改调价计划信息")
    @RequestMapping(value = "/api/AdjustPriceSchedules/{id}", method = RequestMethod.PUT)
    @Auth(allowedBizOp = {ServerConstantValue.GP_ADMIN })
    public ResponseEntity update(@PathVariable("id") Integer id, @RequestBody AdjustPriceSchedule newAdjustPriceSchedule)
    {
        ResponseEntity responseEntity;

        Optional<AdjustPriceSchedule> scheduleOptional = adjustPriceScheduleService.findById(id);
        if (scheduleOptional.isPresent())
        {
            if(scheduleOptional.get().getStatus() == AdjustPriceScheduleStatus.APSEffecitve)
            {
                throw new ServerSideBusinessException("该调价计划已经执行，禁止修改！", HttpStatus.NOT_ACCEPTABLE);
            }

            adjustPriceScheduleService.update(scheduleOptional.get().getId(),newAdjustPriceSchedule);
            responseEntity = ResponseEntity.ok().build();
        }
        else
        {
            responseEntity = ResponseEntity.notFound().build();
        }

        return responseEntity;
    }

    @OperationLog(desc = "删除调价信息")
    @RequestMapping(value = "/api/AdjustPriceSchedules/{id}", method = RequestMethod.DELETE)
    @Auth(allowedBizOp = {ServerConstantValue.GP_ADMIN })
    public ResponseEntity delete(@PathVariable("id") Integer id)
    {
        ResponseEntity responseEntity;

        Optional<AdjustPriceSchedule> scheduleOptional = adjustPriceScheduleService.findById(id);
        if (scheduleOptional.isPresent())
        {
            if(scheduleOptional.get().getStatus() == AdjustPriceScheduleStatus.APSEffecitve)
            {
                throw new ServerSideBusinessException("该调价计划已经执行，禁止删除！", HttpStatus.NOT_ACCEPTABLE);
            }

            adjustPriceScheduleService.deleteById(scheduleOptional.get().getId());
            responseEntity = ResponseEntity.noContent().build();
        }
        else
        {
            responseEntity = ResponseEntity.notFound().build();
        }

        return responseEntity;
    }
}

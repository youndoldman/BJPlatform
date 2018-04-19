package com.donno.nj.controller;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.constant.Constant;
import com.donno.nj.domain.GasCyrChargeSpec;
import com.donno.nj.domain.GasCyrDynDetail;
import com.donno.nj.representation.ListRep;
import com.donno.nj.service.GasCyrChargeSpecService;
import com.donno.nj.service.GasCyrDynDetailService;
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

/*钢瓶检验、压瓶、退还等动态记录*/
@RestController
public class GasCyrDynDetailController
{
    @Autowired
    GasCyrDynDetailService gasCyrDynDetailService ;

    @RequestMapping(value = "/api/GasCyrDynDetail", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "查询钢瓶动态操作信息")
    public ResponseEntity retrieve(@RequestParam(value = "departmentCode", defaultValue = "") String departmentCode,
                                   @RequestParam(value = "gasCyrSpecCode", defaultValue = "") String gasCyrSpecCode,
                                   @RequestParam(value = "operType", required = true) Integer operType,
                                   @RequestParam(value = "orderBy", defaultValue = "") String orderBy,
                                   @RequestParam(value = "pageSize", defaultValue = Constant.PAGE_SIZE) Integer pageSize,
                                   @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo)
    {
        Map params = new HashMap<String,String>();

        if (gasCyrSpecCode.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("departmentCode", departmentCode));
        }

        if (gasCyrSpecCode.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("gasCyrSpecCode", gasCyrSpecCode));
        }


        if (operType != null)
        {
            params.putAll(ImmutableMap.of("operType", operType));
        }

        params.putAll(paginationParams(pageNo, pageSize, orderBy));

        List<GasCyrDynDetail> gasCyrDynDetails = gasCyrDynDetailService.retrieve(params);
        Integer count = gasCyrDynDetailService.count(params);

        return ResponseEntity.ok(ListRep.assemble(gasCyrDynDetails, count));
    }


    @OperationLog(desc = "增加钢瓶动态操作")
    @RequestMapping(value = "/api/GasCyrDynDetail", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody GasCyrDynDetail gasCyrDynDetail, UriComponentsBuilder ucBuilder)
    {
        ResponseEntity responseEntity;

        /*创建*/
        gasCyrDynDetailService.create(gasCyrDynDetail);

        URI uri = ucBuilder.path("/api/GasCyrDynDetail/{id}").buildAndExpand(gasCyrDynDetail.getId()).toUri();
        responseEntity = ResponseEntity.created(uri).build();

        return responseEntity;
    }


    @OperationLog(desc = "修改钢瓶动态信息")
    @RequestMapping(value = "/api/GasCyrDynDetail/{id}", method = RequestMethod.PUT)
    public ResponseEntity update(@PathVariable("id") Integer id,
                                 @RequestBody GasCyrDynDetail newGasCyrDynDetail)
    {
        ResponseEntity responseEntity;

        gasCyrDynDetailService.update(id, newGasCyrDynDetail);

        responseEntity = ResponseEntity.ok().build();

        return responseEntity;
    }



    @OperationLog(desc = "删除钢瓶动态信息")
    @RequestMapping(value = "/api/GasCyrDynDetail/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable("id") Integer id)
    {
        ResponseEntity responseEntity;

        gasCyrDynDetailService.deleteById(id);

        responseEntity = ResponseEntity.noContent().build();

        return responseEntity;
    }
}

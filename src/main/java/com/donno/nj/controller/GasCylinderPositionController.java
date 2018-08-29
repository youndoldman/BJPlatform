package com.donno.nj.controller;

import com.donno.nj.aspect.Auth;
import com.donno.nj.aspect.OperationLog;
import com.donno.nj.constant.Constant;
import com.donno.nj.domain.GasCylinder;
import com.donno.nj.domain.GasCylinderPosition;
import com.donno.nj.representation.ListRep;
import com.donno.nj.service.GasCylinderService;
import com.donno.nj.service.TableStoreService;
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
public class GasCylinderPositionController
{
    @Autowired
    private TableStoreService tableStoreService;

    @RequestMapping(value = "/api/GasCylinderPosition", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "获取钢瓶列表")
    //@Auth(allowedBizOp = {})
    public ResponseEntity retrieve(@RequestParam(value = "number", defaultValue = "") String number,
                                   @RequestParam(value = "startTime", defaultValue = "") String startTime,
                                   @RequestParam(value = "endTime", defaultValue = "") String endTime,
                                   @RequestParam(value = "orderBy", defaultValue = "") String orderBy,
                                   @RequestParam(value = "pageSize", defaultValue = Constant.PAGE_SIZE) Integer pageSize,
                                   @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo)
    {
        Map params = new HashMap<String,String>();

        if (number.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("number", number));
        }

        if (startTime.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("startTime", startTime));
        }

        if (endTime.trim().length() != 0)
        {
            params.putAll(ImmutableMap.of("endTime", endTime));
        }


        params.putAll(paginationParams(pageNo, pageSize, orderBy));

        List<GasCylinderPosition> gasCylinderPositions = tableStoreService.getRange(number,startTime,endTime);

        return ResponseEntity.ok(ListRep.assemble(gasCylinderPositions, gasCylinderPositions.size()));
    }

}

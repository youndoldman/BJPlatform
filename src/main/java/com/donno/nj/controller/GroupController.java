package com.donno.nj.controller;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.constant.Constant;
import com.donno.nj.domain.Group;
import com.donno.nj.representation.ListRep;
import com.donno.nj.service.GroupService;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.donno.nj.util.ParamMapBuilder.paginationParams;
import static com.google.common.collect.Maps.newHashMap;

@RestController
public class GroupController
{

    @Autowired
    private GroupService groupService;

    @RequestMapping(value = "/api/groups", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "获取群组信息列表")
    public ResponseEntity retrieve(@RequestParam(value = "code", defaultValue = "") String code,
                                            @RequestParam(value = "name", defaultValue = "") String name,
                                            @RequestParam(value = "orderBy", defaultValue = "") String orderBy,
                                            @RequestParam(value = "pageSize", defaultValue = Constant.PAGE_SIZE) Integer pageSize,
                                            @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo)
    {
        Map params = newHashMap(ImmutableMap.of("code", code));
        params.putAll(paginationParams(pageNo, pageSize, orderBy));

        List<Group> groups = groupService.retrieve(params);
        Integer total = groupService.count(params);

        return ResponseEntity.ok(ListRep.assemble(groups, total));
    }

}

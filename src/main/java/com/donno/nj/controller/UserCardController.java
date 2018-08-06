package com.donno.nj.controller;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.constant.Constant;
import com.donno.nj.domain.GasCynTray;
import com.donno.nj.domain.UserCard;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.representation.ListRep;
import com.donno.nj.service.GasCynTrayService;
import com.donno.nj.service.GasCynTrayTSService;
import com.donno.nj.service.UserCardService;
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
public class UserCardController
{
    @Autowired
    private UserCardService userCardService;

    @RequestMapping(value = "/api/UserCard", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "获取用户卡列表")
    public ResponseEntity retrieve(
                                   @RequestParam(value = "number", defaultValue = "") String number,
                                   @RequestParam(value = "userId", defaultValue = "") String userId,
                                   @RequestParam(value = "status", required = false) Integer status,
                                   @RequestParam(value = "orderBy", defaultValue = "") String orderBy,
                                   @RequestParam(value = "pageSize", defaultValue = Constant.PAGE_SIZE) Integer pageSize,
                                   @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo)
    {
        Map params = new HashMap<String,String>();

        if (number.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("number", number));
        }

        if (userId.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("userId", userId));
        }

        if (status != null)
        {
            params.putAll(ImmutableMap.of("status", status));
        }

        params.putAll(paginationParams(pageNo, pageSize, orderBy));

        List<UserCard> userCardList = userCardService.retrieve(params);
        Integer count = userCardService.count(params);
        return ResponseEntity.ok(ListRep.assemble(userCardList, count));
    }

    @OperationLog(desc = "创建用户卡")
    @RequestMapping(value = "/api/UserCard", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody UserCard userCard, UriComponentsBuilder ucBuilder)
    {
        ResponseEntity responseEntity;

        userCardService.create(userCard);

        URI uri = ucBuilder.path("/api/UserCard/{number}").buildAndExpand(userCard.getNumber()).toUri();
        responseEntity = ResponseEntity.created(uri).build();

        return responseEntity;
    }

    @OperationLog(desc = "修改用户卡信息")
    @RequestMapping(value = "/api/UserCard/{number}", method = RequestMethod.PUT)
    public ResponseEntity update(@PathVariable("number") String number, @RequestBody UserCard newUserCard)
    {
        ResponseEntity responseEntity;

        userCardService.update(number,newUserCard);
        responseEntity = ResponseEntity.ok().build();

        return responseEntity;
    }

    @OperationLog(desc = "删除用户卡信息")
    @RequestMapping(value = "/api/UserCard/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable("id") Integer id)
    {
        ResponseEntity responseEntity;

        userCardService.deleteById(id);
        responseEntity = ResponseEntity.noContent().build();

        return responseEntity;
    }



    @OperationLog(desc = "用户卡绑定到客户")
    @RequestMapping(value = "/api/UserCard/Bind/{number}", method = RequestMethod.PUT)
    public ResponseEntity bind(@PathVariable("number") String number,
                                          @RequestParam(value = "userId", defaultValue = "") String userId)
    {
        ResponseEntity responseEntity;

        userCardService.bind(number,userId);
        responseEntity = ResponseEntity.ok().build();

        return responseEntity;
    }


    @OperationLog(desc = "用户卡解除绑定")
    @RequestMapping(value = "/api/UserCard/unBind/{number}", method = RequestMethod.PUT)
    public ResponseEntity unBind(@PathVariable("number") String number,
                                            @RequestParam(value = "userId", defaultValue = "") String userId)
    {
        ResponseEntity responseEntity;
        userCardService.unBind(number,userId);
        responseEntity = ResponseEntity.ok().build();

        return responseEntity;
    }
}

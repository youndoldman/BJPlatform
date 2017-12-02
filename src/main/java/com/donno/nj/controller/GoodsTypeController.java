package com.donno.nj.controller;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.constant.Constant;
import com.donno.nj.representation.ListRep;
import com.donno.nj.service.*;
import com.donno.nj.domain.GoodsType;
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
import java.util.Map;
import java.util.List;

import static com.donno.nj.util.ParamMapBuilder.paginationParams;
import static com.google.common.collect.Maps.newHashMap;

@RestController
public class GoodsTypeController
{

    @Autowired
    GoodsTypeService goodsTypeService;

    @RequestMapping(value = "/api/goodsTypes", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "获取商品类型列表")
    public ResponseEntity retrieve(@RequestParam(value = "code", defaultValue = "") String code,
                                            @RequestParam(value = "name", defaultValue = "") String userName,
                                            @RequestParam(value = "orderBy", defaultValue = "") String orderBy,
                                            @RequestParam(value = "pageSize", defaultValue = Constant.PAGE_SIZE) Integer pageSize,
                                            @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo)
    {
        Map params = newHashMap(ImmutableMap.of("code", code));
        params.putAll(paginationParams(pageNo, pageSize, orderBy));

        List<GoodsType> customers = goodsTypeService.retrieve(params);
        Integer count = goodsTypeService.count(params);

        return ResponseEntity.ok(ListRep.assemble(customers, count));
    }

    @OperationLog(desc = "创建商品类型")
    @RequestMapping(value = "/api/goodsTypes", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody GoodsType goodsType, UriComponentsBuilder ucBuilder)
    {

        ResponseEntity responseEntity;
        if (goodsTypeService.findByCode(goodsType.getCode()).isPresent())
        {
            responseEntity =  ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        else
        {
             /*创建*/
            goodsTypeService.create(goodsType);

            URI uri = ucBuilder.path("/api/goodsTypes/{code}").buildAndExpand(goodsType.getCode()).toUri();
            responseEntity = ResponseEntity.created(uri).build();
        }

        return responseEntity;
    }


    @OperationLog(desc = "修改商品类型信息")
    @RequestMapping(value = "/api/goodsTypes/{code}", method = RequestMethod.PUT)
    public ResponseEntity update(@PathVariable("code") String code, @RequestBody GoodsType newGoodsType)
    {
        ResponseEntity responseEntity;

        Optional<GoodsType> goodsType = goodsTypeService.findByCode(code);
        if (goodsType.isPresent())
        {
            /*新的商品类型代码校验，不能已经存在*/
            if (goodsTypeService.findByCode(newGoodsType.getCode()).isPresent())//目标code值已经存在
            {
                responseEntity =  ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            else
            {
                goodsTypeService.update(goodsType.get().getId(), newGoodsType);
                responseEntity = ResponseEntity.ok().build();
            }
        }
        else
        {
            responseEntity = ResponseEntity.notFound().build();
        }

        return responseEntity;
    }

    @OperationLog(desc = "删除用户信息")
    @RequestMapping(value = "/api/goodsTypes/{code}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable("code") String code)
    {
        ResponseEntity responseEntity;

        Optional<GoodsType> goodsType = goodsTypeService.findByCode(code);
        if (goodsType.isPresent())
        {
            goodsTypeService.deleteById(goodsType.get().getId());

            responseEntity = ResponseEntity.noContent().build();
        }
        else
        {
            responseEntity = ResponseEntity.notFound().build();
        }

        return responseEntity;
    }
}

package com.donno.nj.controller;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.constant.Constant;
import com.donno.nj.domain.AdjustPriceHistory;
import com.donno.nj.domain.Goods;
import com.donno.nj.domain.GoodsType;
import com.donno.nj.representation.ListRep;
import com.donno.nj.service.*;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.donno.nj.util.ParamMapBuilder.paginationParams;
import static com.google.common.collect.Maps.newHashMap;

@RestController
public class GoodsController
{
    @Autowired
    private GoodsService goodsService;

    @RequestMapping(value = "/api/Goods", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "获取商品列表")
    public ResponseEntity retrieve(
                                   @RequestParam(value = "province", defaultValue = "") String province,
                                   @RequestParam(value = "city", defaultValue = "") String city,
                                   @RequestParam(value = "county", defaultValue = "") String county,
                                   @RequestParam(value = "typeCode", defaultValue = "") String typeCode,
                                   @RequestParam(value = "typeName", defaultValue = "") String typeName,
                                   @RequestParam(value = "code", defaultValue = "") String code,
                                   @RequestParam(value = "name", defaultValue = "") String name,
                                   @RequestParam(value = "status", required = false) Integer status,
                                   @RequestParam(value = "cstUserId", defaultValue = "") String cstUserId,
                                   @RequestParam(value = "orderBy", defaultValue = "") String orderBy,
                                   @RequestParam(value = "pageSize", defaultValue = Constant.PAGE_SIZE) Integer pageSize,
                                   @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo)
    {
        Map params = new HashMap<String,String>();

        if (province.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("province", province));
        }

        if (city.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("city", city));
        }

        if (county.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("county", county));
        }

        if (typeCode.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("typeCode", typeCode));
        }

        if (typeName.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("typeName", typeName));
        }

        if (code.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("code", code));
        }

        if (name.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("name", name));
        }

        if (status != null)
        {
            params.putAll(ImmutableMap.of("status", status));
        }


        params.putAll(paginationParams(pageNo, pageSize, orderBy));

        List<Goods> goodses = goodsService.retrieve(params,cstUserId);
        Integer count = goodsService.count(params);
        return ResponseEntity.ok(ListRep.assemble(goodses, count));
    }

    @OperationLog(desc = "创建商品")
    @RequestMapping(value = "/api/Goods", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody Goods goods, UriComponentsBuilder ucBuilder)
    {
        ResponseEntity responseEntity;

        goodsService.create(goods);

        URI uri = ucBuilder.path("/api/Goods/{code}").buildAndExpand(goods.getCode()).toUri();
        responseEntity = ResponseEntity.created(uri).build();

        return responseEntity;
    }

    @OperationLog(desc = "修改商品信息")
    @RequestMapping(value = "/api/Goods/{code}", method = RequestMethod.PUT)
    public ResponseEntity update(@PathVariable("code") String code, @RequestBody Goods newGoods)
    {
        ResponseEntity responseEntity;

        goodsService.update(code,newGoods);
        responseEntity = ResponseEntity.ok().build();

        return responseEntity;
    }

    @OperationLog(desc = "删除商品信息")
    @RequestMapping(value = "/api/Goods/{code}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable("code") String code)
    {
        ResponseEntity responseEntity;

        Optional<Goods> goods = goodsService.findByCode(code);
        if (goods.isPresent())
        {
            goodsService.deleteById(goods.get().getId());
            responseEntity = ResponseEntity.noContent().build();
        }
        else
        {
            responseEntity = ResponseEntity.notFound().build();
        }

        return responseEntity;
    }

    @RequestMapping(value = "/api/Goods/PriceHistory", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "获取商品调价历史")
    public ResponseEntity retrieve(@RequestParam(value = "code", defaultValue = "") String code)
    {
        Map params = new HashMap<String,String>();

        if (code.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("code", code));
        }

        List<AdjustPriceHistory> adjustPriceHistorys = goodsService.retrieveAdjustPriceHistory(params);
        Integer count = adjustPriceHistorys.size();
        return ResponseEntity.ok(ListRep.assemble(adjustPriceHistorys, count));
    }
}

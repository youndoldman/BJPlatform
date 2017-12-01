package com.donno.nj.controller;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.constant.Constant;
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
import java.util.List;
import java.util.Map;

import static com.donno.nj.util.ParamMapBuilder.paginationParams;
import static com.google.common.collect.Maps.newHashMap;

@RestController
public class GoodsController
{
    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsTypeService goodsTypeService;


    @RequestMapping(value = "/api/goods", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "获取商品列表")
    public ResponseEntity retrieve(@RequestParam(value = "name", defaultValue = "") String name,
                                   @RequestParam(value = "orderBy", defaultValue = "") String orderBy,
                                   @RequestParam(value = "pageSize", defaultValue = Constant.PAGE_SIZE) Integer pageSize,
                                   @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo)
    {
        Map params = newHashMap(ImmutableMap.of("name", name));
        params.putAll(paginationParams(pageNo, pageSize, orderBy));

        List<Goods> goodss = goodsService.retrieve(params);
        return ResponseEntity.ok(ListRep.assemble(goodss, goodss.size()));
    }

    @OperationLog(desc = "创建商品")
    @RequestMapping(value = "/api/goods", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody Goods goods, UriComponentsBuilder ucBuilder)
    {
        ResponseEntity responseEntity;
        if (goodsService.findByName(goods.getName()).isPresent())
        {
            responseEntity =  ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        else
        {
            /*产品类型信息校验*/
            if (goods.getGoodsType() != null)
            {
                Optional<GoodsType> goodsType =  goodsTypeService.findByCode(goods.getGoodsType().getCode()) ;
                if(!goodsType.isPresent())
                {
                    responseEntity =  ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
                }
                else
                {
                    goods.setGoodsType(goodsType.get());
                    goodsService.create(goods);

                    URI uri = ucBuilder.path("/api/goods/{id}").buildAndExpand(goods.getId()).toUri();
                    responseEntity = ResponseEntity.created(uri).build();
                }
            }
            else
            {
                responseEntity =  ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
            }
        }

        return responseEntity;
    }

    @OperationLog(desc = "修改商品信息")
    @RequestMapping(value = "/api/goods/{name}", method = RequestMethod.PUT)
    public ResponseEntity update(@PathVariable("name") String name, @RequestBody Goods newGoods)
    {
        ResponseEntity responseEntity;

        Optional<Goods> goods = goodsService.findByName(name);
        if (goods.isPresent())
        {
            /*产品类型信息校验*/
            if (newGoods.getGoodsType() != null)
            {
                Optional<GoodsType> goodsType =  goodsTypeService.findByCode(newGoods.getGoodsType().getCode()) ;
                if(!goodsType.isPresent())
                {
                    responseEntity =  ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
                }
                else
                {
                    newGoods.setGoodsType(goodsType.get());
                    goodsService.update(goods.get().getId(),newGoods);
                    responseEntity = ResponseEntity.ok().build();
                }
            }
            else
            {
                responseEntity =  ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
            }
        }
        else
        {
            responseEntity = ResponseEntity.notFound().build();
        }

        return responseEntity;
    }

    @OperationLog(desc = "删除商品信息")
    @RequestMapping(value = "/api/goods/{name}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable("name") String name)
    {
        ResponseEntity responseEntity;

        Optional<Goods> goods = goodsService.findByName(name);
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
}

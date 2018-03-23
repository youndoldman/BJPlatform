package com.donno.nj.controller;
import com.donno.nj.aspect.OperationLog;
import com.donno.nj.constant.Constant;
import com.donno.nj.domain.DiscountStrategy;
import com.donno.nj.representation.ListRep;
import com.donno.nj.service.*;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import static com.donno.nj.util.ParamMapBuilder.paginationParams;

@RestController
public class DiscountStrategyController
{
    @Autowired
    private DiscountStrategyService discountStrategyService;

    @RequestMapping(value = "/api/DiscountStrategies", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "获取商品列表")
    public ResponseEntity retrieve(@RequestParam(value = "name", defaultValue = "") String name,
                                   @RequestParam(value = "status", required = false) Integer status,
                                   @RequestParam(value = "conditionTypeCode", defaultValue = "") String conditionTypeCode,
                                   @RequestParam(value = "discountType", required = false) Integer discountType,
                                   @RequestParam(value = "useType", required = false) Integer useType,
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

        if (conditionTypeCode.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("conditionTypeCode", conditionTypeCode));
        }

        if (discountType != null)
        {
            params.putAll(ImmutableMap.of("discountType", discountType));
        }

        if (useType != null)
        {
            params.putAll(ImmutableMap.of("useType", useType));
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

        List<DiscountStrategy> strategies = discountStrategyService.retrieve(params);
        Integer count = discountStrategyService.count(params);
        return ResponseEntity.ok(ListRep.assemble(strategies, count));
    }

    @OperationLog(desc = "创建优惠策略")
    @RequestMapping(value = "/api/DiscountStrategies", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody DiscountStrategy discountStrategy, UriComponentsBuilder ucBuilder)
    {
        ResponseEntity responseEntity;

        /*创建优惠策略*/
        discountStrategyService.create(discountStrategy);

        URI uri = ucBuilder.path("/api/DiscountStrategies/{id}").buildAndExpand(discountStrategy.getId()).toUri();
        responseEntity = ResponseEntity.created(uri).build();

        return responseEntity;
    }

    @OperationLog(desc = "修改优惠策略信息")
    @RequestMapping(value = "/api/DiscountStrategies/{id}", method = RequestMethod.PUT)
    public ResponseEntity update(@PathVariable("id") Integer id, @RequestBody DiscountStrategy newDiscountStrategy)
    {
        ResponseEntity responseEntity;

        Optional<DiscountStrategy> strategyOptional = discountStrategyService.findById(id);
        if (strategyOptional.isPresent())
        {
            discountStrategyService.update(strategyOptional.get().getId(),newDiscountStrategy);
            responseEntity = ResponseEntity.ok().build();
        }
        else
        {
            responseEntity = ResponseEntity.notFound().build();
        }

        return responseEntity;
    }

    @OperationLog(desc = "删除优惠策略信息")
    @RequestMapping(value = "/api/DiscountStrategies/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable("id") Integer id)
    {
        ResponseEntity responseEntity;

        Optional<DiscountStrategy> strategyOptional = discountStrategyService.findById(id);
        if (strategyOptional.isPresent())
        {
            discountStrategyService.deleteById(strategyOptional.get().getId());
            responseEntity = ResponseEntity.noContent().build();
        }
        else
        {
            responseEntity = ResponseEntity.notFound().build();
        }

        return responseEntity;
    }
}

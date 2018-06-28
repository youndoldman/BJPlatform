package com.donno.nj.service;

import com.donno.nj.domain.Order;
import com.donno.nj.domain.PayType;
import com.donno.nj.domain.UninterruptedGasOrder;
import com.google.common.base.Optional;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
public interface UninterruptGasOrderService
{
    Optional<UninterruptedGasOrder> findBySn(String sn);

    List<UninterruptedGasOrder> retrieve(Map params);

    Integer count(Map params);

    void create(UninterruptedGasOrder order);

    void GasPay(String orderSn, PayType payType,Float emptyWeight);

    void deleteById(Integer id);

    UninterruptedGasOrder GasPayCaculate(String orderSn, Float emptyWeight);

    void weixinPayOk(String orderSn, String weixinSn, Integer amount);


}

package com.donno.nj.service;

import com.donno.nj.domain.Goods;
import com.google.common.base.Optional;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
public interface GoodsService
{
    Optional<Goods> findByName(String name);

    List<Goods> retrieve(Map params);

    Integer count(Map params);

    void create(Goods goods);

    void update(Integer id, Goods newGoods);

    void deleteById(Integer id);
}

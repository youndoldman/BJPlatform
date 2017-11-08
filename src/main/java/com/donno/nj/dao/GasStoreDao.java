package com.donno.nj.dao;

import com.donno.nj.dao.base.BaseDao;

import com.donno.nj.domain.GasStore;

/**
 * Created by T470P on 2017/10/30.
 */
public interface GasStoreDao  extends BaseDao<GasStore>
{
    GasStore findByCode(String code);
}

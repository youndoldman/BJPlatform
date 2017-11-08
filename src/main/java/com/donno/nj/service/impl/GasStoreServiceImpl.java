package com.donno.nj.service.impl;


import com.donno.nj.dao.GasStoreDao;
import com.donno.nj.domain.GasStore;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.donno.nj.service.GasStoreService;
import java.util.List;
import java.util.Map;

@Service
public class GasStoreServiceImpl implements GasStoreService
{
    @Autowired
    private GasStoreDao gasStoreDao;

    @Override
    public Optional<GasStore> findByCode(String code) {
        return Optional.fromNullable(gasStoreDao.findByCode(code));
    }

    @Override
    public List<GasStore> retrieve(Map params) {
        return gasStoreDao.getList(params);
    }

    @Override
    public Integer count(Map params) {
        return gasStoreDao.count(params);
    }

    @Override
    public GasStore create(GasStore gasStore) {
        gasStoreDao.insert(gasStore);
        return gasStore;
    }


    @Override
    public void update(GasStore gasStore, GasStore newGasStore)
    {

    }

    @Override
    public void delete(GasStore gasStore)
    {
        gasStoreDao.delete(gasStore.getId());
    }


}

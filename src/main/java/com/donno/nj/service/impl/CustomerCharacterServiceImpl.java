package com.donno.nj.service.impl;
import com.donno.nj.dao.CustomerCharacterDao;
import com.donno.nj.domain.CustomerCharacter;
import com.donno.nj.service.CustomerCharacterService;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by T470P on 2017/10/30.
 */

@Service
public class CustomerCharacterServiceImpl implements CustomerCharacterService
{
    @Autowired
    private CustomerCharacterDao cstomerCharacterDao;

    @Override
    public Optional<CustomerCharacter> findByCode(String code) {
        return Optional.fromNullable(cstomerCharacterDao.findByCode(code));
    }

    @Override
    public List<CustomerCharacter> retrieve(Map params) {
        return cstomerCharacterDao.getList(params);
    }

    @Override
    public Integer count(Map params) {
        return cstomerCharacterDao.count(params);
    }

    @Override
    public CustomerCharacter create(CustomerCharacter customerCharacter) {
        cstomerCharacterDao.insert(customerCharacter);
        return customerCharacter;
    }

    @Override
    public void update(CustomerCharacter customerCharacter, CustomerCharacter newCustomerCharacter)
    {

    }

    @Override
    public void delete(CustomerCharacter customerCharacter)
    {
        cstomerCharacterDao.delete(customerCharacter.getId());
    }
}

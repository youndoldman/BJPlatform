package com.donno.nj.service.impl;


import com.donno.nj.dao.SecurityCheckTypeDao;
import com.donno.nj.domain.SecurityCheckType;
import com.donno.nj.service.SecurityCheckTypeService;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SecurityCheckTypeServiceImpl implements SecurityCheckTypeService
{

    @Autowired
    private SecurityCheckTypeDao securityCheckTypeDao;

    @Override
    public Optional<SecurityCheckType> findByCode(String code) {
        return Optional.fromNullable(securityCheckTypeDao.findByCode(code));
    }

    @Override
    public List<SecurityCheckType> retrieve(Map params) {
        return securityCheckTypeDao.getList(params);
    }

    @Override
    public Integer count(Map params) {
        return securityCheckTypeDao.count(params);
    }

    @Override
    public SecurityCheckType create(SecurityCheckType customerType) {
        securityCheckTypeDao.insert(customerType);
        return customerType;
    }


    @Override
    public void update(Integer id, SecurityCheckType newSecurityCheckType)
    {
        /*更新基表数据*/
        newSecurityCheckType.setId(id);
        securityCheckTypeDao.update(newSecurityCheckType);
    }

    @Override
    public  void deleteById(Integer id)
    {
        securityCheckTypeDao.delete(id);
    }

}

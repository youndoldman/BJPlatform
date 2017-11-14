package com.donno.nj.service.impl;


import com.donno.nj.dao.GroupDao;
import com.donno.nj.domain.Group;
import com.donno.nj.service.GroupService;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class GroupServiceImpl implements GroupService
{
    @Autowired
    private GroupDao groupDao;


    @Override
    public Optional<Group> findByCode(String code) {
        return Optional.fromNullable(groupDao.findByCode(code));
    }

    @Override
    public List<Group> retrieve(Map params)
    {
        return groupDao.getList(params);
    }

    @Override
    public Integer count(Map params) {
        return groupDao.count(params);
    }

    @Override
    public void create(Group group)
    {
    }


    @Override
    public void update(Group gasStore, Group newGasStore)
    {

    }

    @Override
    public void delete(Group gasStore)
    {
        groupDao.delete(gasStore.getId());
    }


}

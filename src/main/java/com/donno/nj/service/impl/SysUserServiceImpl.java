package com.donno.nj.service.impl;



import com.donno.nj.dao.SysUserDao;
import com.donno.nj.domain.SysUser;
import com.donno.nj.service.SysUserService;

import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SysUserServiceImpl implements SysUserService
{
    @Autowired
    private SysUserDao userDao;

    @Override
    public List<SysUser> retrieve(Map params) {
        return userDao.getList(params);
    }

    @Override
    public Integer count(Map params) {
        return userDao.count(params);
    }

    @Override
    public SysUser create(SysUser user) {
        userDao.insert(user);
        return user;
    }

    @Override
    public Optional<SysUser> findByUserId(String userId) {
        return Optional.fromNullable(userDao.findByUserId(userId));
    }

    @Override
    public void update(SysUser user, SysUser newUser) {
        newUser.setId(user.getId());
        userDao.update(newUser);
    }

    @Override
    public void delete(String userId) {
        userDao.deleteByUserId(userId);
    }

}

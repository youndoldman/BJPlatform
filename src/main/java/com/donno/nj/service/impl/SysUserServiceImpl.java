package com.donno.nj.service.impl;



import com.donno.nj.dao.SysUserDao;
import com.donno.nj.dao.UserDao;
import com.donno.nj.domain.SysUser;
import com.donno.nj.domain.User;
import com.donno.nj.service.SysUserService;


import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SysUserServiceImpl extends UserServiceImpl implements SysUserService
{
    @Autowired
    private UserDao userDao;

    @Autowired
    private SysUserDao sysUserDao;

    @Override
    public List<SysUser> retrieve(Map params) {
        return sysUserDao.getList(params);
    }

    @Override
    public Integer count(Map params) {
        return sysUserDao.count(params);
    }

    @Override
    public void create(SysUser sysUser)
    {
        User user = sysUser;
        long retCode = userDao.insert(sysUser);//插入用户基表数据，自动返回id值到user

        if (retCode > 0)
        {
            sysUserDao.insert(sysUser);//插入用户表数据
        }
    }


    @Override
    public void update(Integer id, SysUser newUser)
    {
        newUser.setId(id);

        /*更新基表数据*/
        userDao.update(newUser);
        sysUserDao.update(newUser);
    }

    @Override
    public void delete(Integer id)
    {
        userDao.delete(id);
        sysUserDao.deleteByUserIdx(id);
    }

}

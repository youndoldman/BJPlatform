package com.donno.nj.service.impl;



import com.donno.nj.dao.SysUserDao;
import com.donno.nj.dao.UserDao;
import com.donno.nj.dao.DepartmentDao;
import com.donno.nj.domain.Department;
import com.donno.nj.domain.Group;
import com.donno.nj.domain.SysUser;
import com.donno.nj.domain.User;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.service.SysUserService;


import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @Autowired
    private DepartmentDao departmentDao;

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

        /*校验用户是否存在*/
        checkUserExist(user.getUserId());

        /*用户组信息校验*/
        checkUserGroup(user);

        /*部门信息校验*/
        checkDepartment(sysUser);

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

         /*校验源信息是否合法*/
        User oldCustomer = userDao.findById(id);
        if ( oldCustomer == null)
        {
            throw new ServerSideBusinessException("用户不存在，不能进行修改操作！",HttpStatus.CONFLICT);
        }

        /*校验目的用户是否已经存在*/
        if (newUser.getUserId() != null )
        {
            if ( newUser.getUserId().equals(oldCustomer.getUserId()) )//userid 不修改
            {
                newUser.setUserId(null);
            }
            else
            {
                checkUserExist(newUser.getUserId());
            }
        }

        /*用户组信息校验*/
        if (newUser.getUserGroup() != null)
        {
            checkUserGroup(newUser);
        }

        /*部门信息校验*/
        if (newUser.getDepartment() != null)
        {
            checkDepartment(newUser);
        }

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

//    @Override
//    public void checkUserGroup(User user)
//    {
//        Group group;
//        if (user.getUserGroup() != null)
//        {
//            if ((user.getUserGroup().getId() != null) && (user.getUserGroup().getId()  != 0) )
//            {
//                 /*用户组信息校验*/
//                group = groupDao.findById(user.getUserGroup().getId());
//                if (group == null)//用户组不存在
//                {
//                    throw new ServerSideBusinessException("用户组信息错误",HttpStatus.NOT_ACCEPTABLE);
//                }
//            }
//            else
//            {
//                throw new ServerSideBusinessException("请填写用户组信息",HttpStatus.NOT_ACCEPTABLE);
//            }
//        }
//        else
//        {
//            throw new ServerSideBusinessException("请填写用户组信息",HttpStatus.NOT_ACCEPTABLE);
//        }
//    }


    public void checkDepartment(SysUser user)
    {
        Department department;
        if (user.getDepartment() != null)
        {
            if (user.getDepartment().getCode() != null)
            {
                department = departmentDao.findByCode(user.getDepartment().getCode());
                if ( department == null)
                {
                    throw new ServerSideBusinessException("部门信息错误,没有该部门信息",HttpStatus.NOT_ACCEPTABLE);
                }
            }
            else
            {
                throw new ServerSideBusinessException("部门信息错误,缺少部门名称",HttpStatus.NOT_ACCEPTABLE);
            }

        }
        else
        {
            throw new ServerSideBusinessException("部门信息错误,缺少部门名称",HttpStatus.NOT_ACCEPTABLE);
        }
        user.setDepartment(department);
    }

}

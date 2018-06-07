package com.donno.nj.service.impl;



import com.donno.nj.aspect.OperationLog;
import com.donno.nj.dao.*;
import com.donno.nj.domain.*;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.service.SysUserService;

import com.donno.nj.service.TableStoreService;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
@Service
public class SysUserServiceImpl extends UserServiceImpl implements SysUserService
{
    @Autowired
    private UserDao userDao;

    @Autowired
    private SysUserDao sysUserDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private UserPositionDao userPositionDao;

    @Autowired
    private GasCylinderDao gasCylinderDao;

    @Autowired
    private TableStoreService tableStoreService;


    @Override
    @OperationLog(desc = "根据用户ID查询客户信息")
    public Optional<SysUser> findByUId(String userId)
    {
        return Optional.fromNullable(sysUserDao.findByUId(userId));
    }

    @Override
    public List<SysUser> retrieve(Map params) {
        return sysUserDao.getList(params);
    }

    @Override
    public Integer count(Map params) {
        return sysUserDao.count(params);
    }

    public void checkAlive()
    {
        sysUserDao.checkAlive(new Date());
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

        userDao.insert(sysUser);//插入用户基表数据，自动返回id值到user
        sysUserDao.insert(sysUser);//插入用户表数据

    }


    @Override
    public void update(Integer id, SysUser newUser)
    {
        newUser.setId(id);

         /*校验源信息是否合法*/
        User oldCustomer = userDao.findById(id);
        if ( oldCustomer == null)
        {
            throw new ServerSideBusinessException("用户不存在，不能进行修改操作！",HttpStatus.NOT_FOUND);
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
    public void updatePosition(String userId, UserPosition userPosition)
    {
        /*用户校验，参数不能为空，内容不能为空，用户应存在*/
        if (userId != null && userId.trim().length() > 0 )
        {
            SysUser user = sysUserDao.findByUId(userId);
            if (user != null)
            {
                /*位置信息如果存在则更新，如果不存在则添加*/
                Map params = new HashMap<String,String>();
                params.putAll(ImmutableMap.of("userId", userId));
                List<UserPosition> targetPosition = userPositionDao.getList(params);
                if (targetPosition.size() == 0)
                {
                    userPosition.setUserIdx(user.getId());
                    userPositionDao.insert(userPosition);
                }
                else //更新位置信息
                {
                    userPosition.setId(targetPosition.get(0).getId());
                    userPositionDao.update(userPosition);
                }

                /*查询用户名下的钢瓶，更新钢瓶的位置信息*/
                List<GasCylinderPosition> gasCylinderPosList = new ArrayList<GasCylinderPosition>();

                params.clear();
                params.putAll(ImmutableMap.of("liableUserId", userId));
                List<GasCylinder> gasCylinderLst= gasCylinderDao.getList(params);
                for (GasCylinder gasCylinder:gasCylinderLst)
                {
                    /*实时位置*/
                    gasCylinder.setLongitude(userPosition.getLongitude());
                    gasCylinder.setLatitude(userPosition.getLatitude());
                    gasCylinderDao.update(gasCylinder);

                    /*历史轨迹*/
                    GasCylinderPosition gasPosition = new GasCylinderPosition();
                    gasPosition.setCode(gasCylinder.getNumber());
                    String position =  userPosition.getLongitude().toString();
                    position = position.concat(",");
                    position = position.concat(userPosition.getLatitude().toString());
                    gasPosition.setLocation(position);
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                    gasPosition.setCreateTime(df.format(new Date()));

                    gasCylinderPosList.add(gasPosition);

                }

                if (gasCylinderPosList.size() > 0)
                {
                    tableStoreService.batchWriteRow(gasCylinderPosList);
                }
            }
            else
            {
                throw new ServerSideBusinessException("用户不存在",HttpStatus.NOT_FOUND);
            }
        }
        else
        {
            throw new ServerSideBusinessException("用户信息不能为空！",HttpStatus.NOT_ACCEPTABLE);
        }
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

    @Override
    public List<SysUser> getDepLeaderByUserId(String userId,String groupCode)
    {
         /*用户组信息校验*/
        Group group = groupDao.findByCode(groupCode.trim());
        if (groupCode == null || group == null)//用户组不存在
        {
            throw new ServerSideBusinessException("用户组信息错误，不存在用户组" ,HttpStatus.NOT_ACCEPTABLE);
        }

        /*用户信息校验*/
        SysUser user = sysUserDao.findByUId(userId);
        if (userId == null || user == null)
        {
            throw new ServerSideBusinessException("用户信息错误，不存在用户" ,HttpStatus.NOT_ACCEPTABLE);
        }

        List<SysUser> sysUsers = sysUserDao.getDepLeaderByUserId(userId,groupCode);

        return sysUsers;
    }
}

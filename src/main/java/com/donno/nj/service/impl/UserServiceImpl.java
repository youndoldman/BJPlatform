package com.donno.nj.service.impl;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.dao.CustomerAddressDao;
import com.donno.nj.dao.CustomerDao;
import com.donno.nj.dao.GroupDao;
import com.donno.nj.dao.UserDao;
import com.donno.nj.domain.Customer;
import com.donno.nj.domain.Group;
import com.donno.nj.domain.User;
import com.donno.nj.service.UserService;
import com.donno.nj.service.UserService;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import com.donno.nj.exception.ServerSideBusinessException;
import java.util.List;
import java.util.Map;


@Service
public class UserServiceImpl  implements UserService
{
    @Autowired
    protected UserDao userDao;

    @Autowired
    protected GroupDao groupDao;

    @Override
    @OperationLog(desc = "根据用户ID查询客户信息")
    public Optional<User> findByUserId(String userId)
    {
    return Optional.fromNullable(userDao.findByUserId(userId));
}

    @Override
    @OperationLog(desc = "根据用户ID查询客户信息")
    public Optional<User> findById(Integer id) {
        return Optional.fromNullable(userDao.findById(id));
    }

    @OperationLog(desc = "检查目标用户是否存在")
    public void checkUserExist(String userId)
    {
        if (userDao.findByUserId(userId) != null)
        {
            throw new ServerSideBusinessException("用户"+userId+"已经存在",HttpStatus.CONFLICT);
        }
    }




    public void checkUserGroup(User user)
    {
        Group group;
        if (user.getUserGroup() != null)
        {
            if ((user.getUserGroup().getCode() != null) && (user.getUserGroup().getCode().trim().length() != 0) )
            {
                 /*用户组信息校验*/
                group = groupDao.findByCode(user.getUserGroup().getCode());
                if (group == null)//用户组不存在
                {
                    throw new ServerSideBusinessException("用户组信息错误，不存在用户组"+user.getUserGroup().getCode(),HttpStatus.NOT_ACCEPTABLE);
                }
            }
            else
            {
                throw new ServerSideBusinessException("请填写用户组信息",HttpStatus.NOT_ACCEPTABLE);
            }
        }
        else
        {
            throw new ServerSideBusinessException("请填写用户组信息",HttpStatus.NOT_ACCEPTABLE);
        }

        user.setUserGroup(group);
    }

}

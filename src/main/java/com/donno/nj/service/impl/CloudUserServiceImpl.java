package com.donno.nj.service.impl;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.dao.*;
import com.donno.nj.domain.*;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.service.CloudUserService;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class CloudUserServiceImpl implements CloudUserService
{

    @Autowired
    private CloudUserDao cloudUserDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private CloudPanvaUserBindRelationDao cloudPanvaUserBindRelationDao;


    @Override
    public Optional<CloudUser> findByCloudUserId(String cloudUserId)
    {
        return Optional.fromNullable(cloudUserDao.findByCloudUserId(cloudUserId));
    }

    @Override
    @OperationLog(desc = "查询云客服信息")
    public List<CloudUser> retrieve(Map params)
    {
        List<CloudUser> cloudUsers = cloudUserDao.getList(params);
        return cloudUsers;
    }

    @Override
    @OperationLog(desc = "查询云客服数量")
    public Integer count(Map params) {
        return cloudUserDao.count(params);
    }

    @Override
    @OperationLog(desc = "创建云客服信息")
    public void create(CloudUser cloudUser)
    {
        /*参数校验*/
        if (cloudUser == null || cloudUser.getUserId() == null || cloudUser.getUserId().trim().length() == 0 )
        {
            throw new ServerSideBusinessException("云客服用户信息不全，缺少用户名称，请补充！", HttpStatus.NOT_ACCEPTABLE);
        }

        if (cloudUser.getPassword() == null || cloudUser.getPassword().trim().length() == 0)
        {
            throw new ServerSideBusinessException("云客服用户信息不全，缺少用户密码，请补充！", HttpStatus.NOT_ACCEPTABLE);
        }


        /*云客服用户是否已经存在*/
        if (findByCloudUserId(cloudUser.getUserId()).isPresent())
        {
            throw new ServerSideBusinessException("云客服用户信息已经存在！", HttpStatus.CONFLICT);
        }

        cloudUserDao.insert(cloudUser);
    }


    @Override
    @OperationLog(desc = "修改云客服信息")
    public void update(String userId, CloudUser newCloudUser)
    {
        /*参数校验*/
        if (userId == null || userId.trim().length() == 0 || newCloudUser == null )
        {
            throw new ServerSideBusinessException("云客服用户信息不能为空！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*云客服用户是否存在*/
        Optional<CloudUser> cloudUserOptional = findByCloudUserId(userId);
        if (!cloudUserOptional.isPresent())
        {
            throw new ServerSideBusinessException("云客服用户信息不存在！", HttpStatus.NOT_FOUND);
        }

        CloudUser cloudUser = findByCloudUserId(userId).get();
        newCloudUser.setId(cloudUser.getId());

        /*云客服用户新ID校验*/
        if (newCloudUser.getUserId() != null && newCloudUser.getUserId().trim().length() > 0 )
        {
            if (userId.equals(newCloudUser.getUserId()))//用户ID不修改
            {
                newCloudUser.setUserId(null);
            }
            else
            {
                /*目标用户是否存在*/
                if (findByCloudUserId(newCloudUser.getUserId()).isPresent())
                {
                    throw new ServerSideBusinessException("云客服已经存在！", HttpStatus.CONFLICT);
                }
            }
        }
        else
        {
            newCloudUser.setUserId(null);
        }

        /*更新数据*/
        cloudUserDao.update(newCloudUser);
    }



    @Override
    @OperationLog(desc = "删除云客服信息")
    public void delete(Integer id)
    {
        CloudUser cloudUser = cloudUserDao.findById(id);
        if (cloudUser == null)
        {
            throw new ServerSideBusinessException("云客服不存在！",HttpStatus.NOT_FOUND);
        }

        /*检查，如果有绑定关系，则解绑定*/
        User user = cloudPanvaUserBindRelationDao.findPanvaUserByCloudUserIdx(id);
        if (user != null)
        {
            cloudPanvaUserBindRelationDao.unBindCloudPannaUser(id,user.getId());
        }

        /*钢瓶基础信息表数据删除*/
        cloudUserDao.delete(id);
    }

    @Override
    @OperationLog(desc = "解除绑定关系")
    public void unBindPanvaUser(String cloudUserId,String panvaUserId)
    {
        /*参数校验*/
        if (cloudUserId == null || cloudUserId.trim().length() == 0 )
        {
            throw new ServerSideBusinessException("参数错误，缺少云客服用户ID！", HttpStatus.NOT_ACCEPTABLE);
        }

        if (panvaUserId == null || panvaUserId.trim().length() == 0 )
        {
            throw new ServerSideBusinessException("参数错误，缺少系统用户ID！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*云客服是否存在*/
        Optional<CloudUser> cloudUserOptional = findByCloudUserId(cloudUserId);
        if ( !cloudUserOptional.isPresent())
        {
            throw new ServerSideBusinessException("云客服信息不存在！", HttpStatus.NOT_FOUND);
        }
        CloudUser cloudUser = cloudUserOptional.get();

        /*系统用户是否存在*/
        User user = userDao.findByUserId(panvaUserId);
        if (user == null)
        {
            throw new ServerSideBusinessException("系统用户新息不存在，请先创建系统用户！", HttpStatus.NOT_FOUND);
        }

        /*如果云客服和系统用户没有被绑定，提示错误信息*/
        if ( cloudPanvaUserBindRelationDao.findBindRelation(cloudUser.getId(),user.getId()) == null)
        {
            throw new ServerSideBusinessException("云客服和系统用户没有被绑定！", HttpStatus.NOT_FOUND);
        }

        cloudPanvaUserBindRelationDao.unBindCloudPannaUser(cloudUser.getId(),user.getId());
    }

    @Override
    @OperationLog(desc = "建立绑定关系")
    public void bindPanvaUser(String cloudUserId,String panvaUserId)
    {
        /*参数校验*/
        if (cloudUserId == null || cloudUserId.trim().length() == 0 )
        {
            throw new ServerSideBusinessException("缺少云客服用户信息！", HttpStatus.NOT_ACCEPTABLE);
        }

        if (panvaUserId == null || panvaUserId.trim().length() == 0 )
        {
            throw new ServerSideBusinessException("缺少系统用户信息！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*云客服是否存在*/
        Optional<CloudUser> cloudUserOptional = findByCloudUserId(cloudUserId);
        if ( !cloudUserOptional.isPresent())
        {
            throw new ServerSideBusinessException("云客服信息不存在！", HttpStatus.NOT_FOUND);
        }
        CloudUser cloudUser = cloudUserOptional.get();

        /*系统用户是否存在*/
        User user = userDao.findByUserId(panvaUserId);
        if (user == null)
        {
            throw new ServerSideBusinessException("系统用户新息不存在，请先创建系统用户！", HttpStatus.NOT_FOUND);
        }

        /*如果云客服已经被绑定了，提示应先解绑定*/
        if ( cloudPanvaUserBindRelationDao.findPanvaUserByCloudUserIdx(user.getId()) != null)
        {
            throw new ServerSideBusinessException("云客服用户已经绑定，请先解绑定！", HttpStatus.CONFLICT);
        }

        /*如果系统用户已经被绑定了，提示应先解绑定*/
        if ( cloudPanvaUserBindRelationDao.findCloudUserByPanvaUserIdx(cloudUser.getId()) != null)
        {

            throw new ServerSideBusinessException("系统用户已经绑定，请先解绑定！", HttpStatus.CONFLICT);
        }

        cloudPanvaUserBindRelationDao.bindCloudPannaUser(cloudUser.getId(),user.getId());
    }




}

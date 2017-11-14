package com.donno.nj.activiti;

import java.util.ArrayList;
import java.util.List;


import com.google.common.base.Optional;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.UserQueryImpl;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.IdentityInfoEntity;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.engine.impl.persistence.entity.UserEntityManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import com.donno.nj.service.UserService;
import com.donno.nj.service.GroupService;


/**
 * 自定义的Activiti用户管理器
 *
 * @author dragon
 *
 */

@Service
public class CustomUserEntityManager extends UserEntityManager {
    private static final Log logger = LogFactory
            .getLog(CustomUserEntityManager.class);

    @Autowired
    private UserService userServiceImpl;

    @Autowired
    private GroupService groupService;

    @Override
    public UserEntity findUserById(final String userCode) {
        if (userCode == null)
            return null;

        try {
            UserEntity userEntity = null;
            com.donno.nj.domain.User bUser = userServiceImpl.findByUserId(userCode).get();
            userEntity = ActivitiUtils.toActivitiUser(bUser);
            return userEntity;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Group> findGroupsByUser(final String userCode) {
        if (userCode == null)
            return null;
        Optional<com.donno.nj.domain.User> validUser = userServiceImpl.findByUserId(userCode);
        List<Group> gs = new ArrayList<Group>();
        GroupEntity g =  ActivitiUtils.toActivitiGroup(validUser.get().getUserGroup());;
        gs.add(g);
        return gs;
    }

    @Override
    public List<User> findUserByQueryCriteria(UserQueryImpl query, Page page) {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public IdentityInfoEntity findUserInfoByUserIdAndKey(String userId,
                                                         String key) {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public List<String> findUserInfoKeysByUserIdAndType(String userId,
                                                        String type) {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public long findUserCountByQueryCriteria(UserQueryImpl query) {
        throw new RuntimeException("not implement method.");
    }
}

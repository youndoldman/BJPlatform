package com.donno.nj.activiti;

import java.util.ArrayList;
import java.util.List;

import com.donno.nj.activiti.ActivitiUtils;
import com.donno.nj.domain.User;
import com.google.common.base.Optional;
import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.GroupQueryImpl;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.GroupEntityManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.donno.nj.service.UserService;



@Service
public class CustomGroupEntityManager extends GroupEntityManager {
    private static final Log logger = LogFactory
            .getLog(CustomGroupEntityManager.class);

    @Autowired
    private UserService userServiceImpl;

    @Override
    public List<Group> findGroupsByUser(final String userCode) {
        if (userCode == null)
            return null;
        Optional<User> validUser = userServiceImpl.findByUserId(userCode);
        User user_temp = new User();
        if (validUser.isPresent())
        {
            user_temp = validUser.get();
        }
        GroupEntity g = ActivitiUtils.toActivitiGroup(user_temp.getUserGroup());
        List<Group> gs = new ArrayList<Group>();
        gs.add(g);
        return gs;
    }

    @Override
    public List<Group> findGroupByQueryCriteria(GroupQueryImpl query, Page page) {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public long findGroupCountByQueryCriteria(GroupQueryImpl query) {
        throw new RuntimeException("not implement method.");
    }
}

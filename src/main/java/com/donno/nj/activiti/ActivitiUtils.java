package com.donno.nj.activiti;

import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.engine.impl.persistence.entity.GroupEntity;

import com.donno.nj.domain.Group;
import com.donno.nj.domain.User;

import java.util.ArrayList;
import java.util.List;




public class ActivitiUtils {


    public  static UserEntity  toActivitiUser(User bUser){
        UserEntity userEntity = new UserEntity();
        userEntity.setId(bUser.getUserId());
        userEntity.setFirstName(bUser.getName());
        userEntity.setPassword(bUser.getPassword());
        userEntity.setRevision(1);
        return userEntity;
    }

    public  static GroupEntity  toActivitiGroup(Group bGroup){
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setRevision(1);
        groupEntity.setType("assignment");

        groupEntity.setId(String.valueOf(bGroup.getId()));
        groupEntity.setName(bGroup.getName());
        return groupEntity;
    }

    public  static List<org.activiti.engine.identity.Group> toActivitiGroups(List<Group> bGroups){

        List<org.activiti.engine.identity.Group> groupEntitys = new ArrayList<org.activiti.engine.identity.Group>();

        for (Group bGroup : bGroups) {
            GroupEntity groupEntity = toActivitiGroup(bGroup);
            groupEntitys.add(groupEntity);
        }
        return groupEntitys;
    }

}
package com.donno.nj.service;

import com.donno.nj.domain.Group;
import com.google.common.base.Optional;

import java.util.List;
import java.util.Map;


public interface GroupService
{

    Optional<Group> findByCode(String code);

    List<Group> retrieve(Map params);

    Integer count(Map params);

    void create(Group group);

    void update(Group group, Group newGroup);

    void delete(Group group);
}

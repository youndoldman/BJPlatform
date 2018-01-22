package com.donno.nj.service;

import com.donno.nj.domain.CloudUser;

import com.google.common.base.Optional;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

@Transactional
public interface CloudUserService
{
    Optional<CloudUser> findByCloudUserId(String cloudUserId);

    List<CloudUser> retrieve(Map params);

    Integer count(Map params);

    void create(CloudUser cloudUser);

    void update(String userId, CloudUser newCloudUser);

    void delete(Integer id);

    void bindPanvaUser(String cloudUserId,String panvaUserId);

    void unBindPanvaUser(String cloudUserId,String panvaUserId);
}




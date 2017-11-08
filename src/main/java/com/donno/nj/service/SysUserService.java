package com.donno.nj.service;

import com.donno.nj.domain.SysUser;
import com.google.common.base.Optional;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Transactional
public interface SysUserService
{
    Optional<SysUser> findByUserId(String userId);

    List<SysUser> retrieve(Map params);

    Integer count(Map params);

    SysUser create(SysUser user);

    void update(SysUser user, SysUser newUser);

    void delete(String userId);
}

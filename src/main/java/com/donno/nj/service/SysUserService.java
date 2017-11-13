package com.donno.nj.service;

import com.donno.nj.domain.SysUser;
import com.google.common.base.Optional;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Transactional
public interface SysUserService extends UserService
{
    List<SysUser> retrieve(Map params);

    Integer count(Map params);

    void create(SysUser user);

    void update(Integer id, SysUser newUser);

    void delete(Integer id);
}

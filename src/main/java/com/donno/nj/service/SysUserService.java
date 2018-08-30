package com.donno.nj.service;

import com.donno.nj.domain.SysUser;
import com.donno.nj.domain.UserPosition;
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

    void updatePosition(String userId, UserPosition userPosition);

    void delete(Integer id);

    void checkAlive();

    List<SysUser> getDepLeaderByUserId(String userId,String groupCode);


    Optional<SysUser> findBySysUserId(String userId);

    /*上传用户照片*/
    void uploadPhoto(String userId, byte[] photo);
    /*获取用户照片*/
    byte[] downloadPhoto(String userId);



}

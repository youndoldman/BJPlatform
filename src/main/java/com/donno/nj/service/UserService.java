package com.donno.nj.service;

import com.donno.nj.domain.User;
import com.google.common.base.Optional;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public interface UserService
{
    Optional<User> findByUserId(String userId);
    Optional<User> findByUserIdPwd(String userId,String password);
    Optional<User> findById(Integer id);


}

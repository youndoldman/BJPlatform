package com.donno.nj.service.impl;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.dao.CustomerAddressDao;
import com.donno.nj.dao.CustomerDao;
import com.donno.nj.dao.UserDao;
import com.donno.nj.domain.Customer;
import com.donno.nj.domain.User;
import com.donno.nj.service.UserService;
import com.donno.nj.service.UserService;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class UserServiceImpl  implements UserService {

    @Autowired
    private UserDao userDao;


    @Override
    @OperationLog(desc = "根据用户ID查询客户信息")
    public Optional<User> findByUserId(String userId) {
        return Optional.fromNullable(userDao.findByUserId(userId));
    }


}

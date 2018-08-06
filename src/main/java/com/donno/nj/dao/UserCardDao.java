package com.donno.nj.dao;


import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.GasCynTray;
import com.donno.nj.domain.UserCard;

public interface UserCardDao extends BaseDao<UserCard>
{
    UserCard findByNumber(String number);
}

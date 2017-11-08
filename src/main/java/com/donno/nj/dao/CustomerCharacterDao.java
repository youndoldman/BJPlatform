package com.donno.nj.dao;

import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.CustomerCharacter;

/**
 * Created by T470P on 2017/10/30.
 */
public interface CustomerCharacterDao extends BaseDao<CustomerCharacter>
{
    CustomerCharacter findByCode(String code);
}

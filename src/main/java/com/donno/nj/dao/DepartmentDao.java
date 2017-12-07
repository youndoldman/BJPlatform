package com.donno.nj.dao;

import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.Department;


public interface DepartmentDao extends BaseDao<Department>
{
    Department findByCode(String code);
}
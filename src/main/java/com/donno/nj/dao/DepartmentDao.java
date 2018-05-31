package com.donno.nj.dao;

import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.Department;

import java.util.List;
import java.util.Map;


public interface DepartmentDao extends BaseDao<Department>
{
    Department findByCode(String code);
    List<Department> findSubDep(Integer id);

    List<Department> getSubDepList(Map params);
    Integer countSubDep(Map m);


}

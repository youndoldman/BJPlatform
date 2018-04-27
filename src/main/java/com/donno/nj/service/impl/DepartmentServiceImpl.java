package com.donno.nj.service.impl;


import com.donno.nj.dao.DepartmentDao;
import com.donno.nj.dao.SysUserDao;
import com.donno.nj.domain.Department;
import com.donno.nj.domain.SysUser;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.service.DepartmentService;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DepartmentServiceImpl implements DepartmentService
{

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private SysUserDao sysUserDao;

    @Override
    public Optional<Department> findByCode(String code)
    {
        return Optional.fromNullable(departmentDao.findByCode(code));
    }

    @Override
    public List<Department> retrieve(Map params) {
        return departmentDao.getList(params);
    }

    @Override
    public List<Department> retrieveSubDepartment(Map params) {
        return departmentDao.getSubDepList(params);
    }



    @Override
    public Integer count(Map params) {
        return departmentDao.count(params);
    }

    @Override
    public Integer countSubDepartment(Map params)
    {
        return departmentDao.countSubDep(params);
    }

    @Override
    public void create(Department department)
    {
        /*参数校验*/
        if (department.getCode() == null || department.getCode().trim().length() ==0)
        {
            throw new ServerSideBusinessException("请填写部门编码信息", HttpStatus.NOT_ACCEPTABLE);
        }

        if (department.getName() == null || department.getName().trim().length() == 0)
        {
            throw new ServerSideBusinessException("请填写部门名称信息", HttpStatus.NOT_ACCEPTABLE);
        }

        /*上级部门信息*/
        if (department.getParentDepartment() == null)
        {
            throw new ServerSideBusinessException("请填写上级部门信息", HttpStatus.NOT_ACCEPTABLE);
        }
        else
        {
            if (department.getParentDepartment().getCode() == null)
            {
                throw new ServerSideBusinessException("请填写上级部门信息", HttpStatus.NOT_ACCEPTABLE);
            }
            else
            {
                Department parentDep = departmentDao.findByCode(department.getParentDepartment().getCode());
                if (parentDep == null)
                {
                    throw new ServerSideBusinessException("上级部门信息信息错误，无法创建", HttpStatus.NOT_ACCEPTABLE);
                }
                else
                {
                    if (parentDep.getCode().equals(department.getCode()) )//上级部门不能为自己
                    {
                        throw new ServerSideBusinessException("上级部门不能为本部门，无法创建", HttpStatus.NOT_ACCEPTABLE);
                    }
                    else
                    {
                        department.setParentDepartment(parentDep);
                    }
                }
            }
        }

        /*检查是否已经存在*/
        if (departmentDao.findByCode(department.getCode()) != null)
        {
            throw new ServerSideBusinessException("部门编码信息已经存在", HttpStatus.CONFLICT);
        }

        departmentDao.insert(department);
    }


    @Override
    public void update(Integer id, Department newDepartment)
    {
        newDepartment.setId(id);

        /*目的部门编码是否已经存在*/
        if ( newDepartment.getCode() != null && newDepartment.getCode().trim().length() !=0)
        {
            Department department = departmentDao.findByCode(newDepartment.getCode());
            if (department != null)
            {
                if (department.getId() != id)
                {
                    throw new ServerSideBusinessException("部门编码信息已经存在", HttpStatus.CONFLICT);
                }
                else
                {
                    newDepartment.setCode(null);
                }
            }
        }

        departmentDao.update(newDepartment);
    }

    @Override
    public void delete(Map params)
    {
        List<Department> departments = retrieveSubDepartment(params);
        for(Department dep : departments)
        {
            /*检查是否有子部门，如果有子部门，需要先删除子部门*/
            if((dep.getLstSubDepartment() != null) && (dep.getLstSubDepartment().size() > 0))
            {
                throw new ServerSideBusinessException("请先删除下级部门！", HttpStatus.NOT_ACCEPTABLE);
            }

             /*检查部门下是否有用户，如果有用户需要提示先删除用户*/
            Map usersParams = new HashMap<String,String>();
            usersParams.putAll(ImmutableMap.of("departmentCode", dep.getCode()));
            Integer userCount = sysUserDao.count(usersParams);
            if (userCount > 0)
            {
                throw new ServerSideBusinessException("部门下有用户存在，请先删除用户", HttpStatus.NOT_ACCEPTABLE);
            }
        }

        departmentDao.delete(params);
    }


}

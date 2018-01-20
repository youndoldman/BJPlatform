package com.donno.nj.service;

import com.donno.nj.domain.Department;
import com.google.common.base.Optional;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
public interface DepartmentService
{
    Optional<Department> findByCode(String code);

    List<Department> retrieve(Map params);

    List<Department> retrieveSubDepartment(Map params);

    void create(Department department);

    Integer count(Map params);

    Integer countSubDepartment(Map params);

    void update(Integer id, Department newDepartment);

    void delete(Map params);
}

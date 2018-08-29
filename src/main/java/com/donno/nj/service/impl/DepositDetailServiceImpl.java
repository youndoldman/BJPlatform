package com.donno.nj.service.impl;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.dao.*;
import com.donno.nj.domain.*;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.service.DepositDetailService;
import com.donno.nj.service.WriteOffDetailService;
import com.donno.nj.util.AppUtil;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DepositDetailServiceImpl implements DepositDetailService
{
    @Autowired
    private DepositDetailDao depositDetailDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Override
    @OperationLog(desc = "查询存入银行记录信息")
    public List<DepositDetail> retrieve(Map params)
    {
        List<DepositDetail> depositDetails = new ArrayList<DepositDetail>();

        if (params.containsKey("departmentCode"))
        {
            recurseRetreve(params,depositDetails);
        }
        else
        {
            depositDetails = depositDetailDao.getList(params);
        }

        return depositDetails;
    }


    /*子公司递归统计*/
    public void recurseRetreve(Map params, List<DepositDetail> depositDetailList)
    {
        String departmentCode = params.get("departmentCode").toString();
        Department department = departmentDao.findByCode(departmentCode);
        if(department == null)
        {
            throw new ServerSideBusinessException("部门信息不存在！", HttpStatus.NOT_ACCEPTABLE);
        }
        department.setLstSubDepartment(departmentDao.findSubDep(department.getId()));
        if ( department.getLstSubDepartment() != null && department.getLstSubDepartment().size() > 0 )
        {
            for (Department childDep : department.getLstSubDepartment())
            {
                Map subParam = new HashMap<String,String>();
                subParam.putAll(params);
                subParam.remove("departmentCode");
                subParam.putAll(ImmutableMap.of("departmentCode", childDep.getCode()));

                List<DepositDetail> subDepositDetail = new ArrayList<DepositDetail>();
                recurseRetreve(subParam,subDepositDetail);

                depositDetailList.addAll(subDepositDetail);
            }
        }
        else
        {
            depositDetailList.addAll(depositDetailDao.getList(params));
        }
    }

    @Override
    @OperationLog(desc = "查询存入银行记录信息条数")
    public Integer count(Map params) {
        return depositDetailDao.count(params);
    }


    @Override
    @OperationLog(desc = "增加存入银行记录")
    public void create(DepositDetail depositDetail)
    {
        /*参数校验*/
        if (depositDetail == null)
        {
            throw new ServerSideBusinessException("入银行款信息不全，请补充信息！", HttpStatus.NOT_ACCEPTABLE);
        }

        if (depositDetail.getOperTime() == null)
        {
            depositDetail.setOperTime(new Date());
        }

        /*操作员信息*/
        Optional<User> userOptional =  AppUtil.getCurrentLoginUser();
        if (!userOptional.isPresent())
        {
            throw new ServerSideBusinessException("未读取到操作员信息，请重新登录！", HttpStatus.NOT_ACCEPTABLE);
        }

        depositDetail.setOperId(userOptional.get().getUserId());

        /*添加记录*/
        depositDetailDao.insert(depositDetail);
    }



    @Override
    @OperationLog(desc = "删除存入银行记录信息")
    public void deleteById(Integer id)
    {
        DepositDetail depositDetail = depositDetailDao.findById(id);
        if (depositDetail == null)
        {
            throw new ServerSideBusinessException("入银行款记录信息不存在！", HttpStatus.NOT_ACCEPTABLE);
        }

        depositDetailDao.delete(id);
    }

    @Override
    @OperationLog(desc = "修改入银行款信息")
    public void update(Integer id, DepositDetail   newDepositDetail)
    {
        /*参数校验*/
        if (newDepositDetail == null )
        {
            throw new ServerSideBusinessException("入银行款信息不能为空！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*客户回款是否存在*/
        DepositDetail target = depositDetailDao.findById(id);
        if (target == null)
        {
            throw new ServerSideBusinessException("该入银行款数据不存在！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*更新数据*/
        newDepositDetail.setId(id);
        depositDetailDao.update(newDepositDetail);
    }
}

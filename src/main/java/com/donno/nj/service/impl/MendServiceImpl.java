package com.donno.nj.service.impl;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.dao.MendDao;
import com.donno.nj.dao.MendTypeDao;
import com.donno.nj.dao.CustomerDao;
import com.donno.nj.dao.SysUserDao;
import com.donno.nj.dao.DepartmentDao;
import com.donno.nj.domain.*;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.service.MendService;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class MendServiceImpl implements MendService
{

    @Autowired
    private MendDao mendDao;

    @Autowired
    private MendTypeDao mendTypeDao;

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private SysUserDao sysUserDao;

    @Autowired
    private DepartmentDao departmentDao;




    @Override
    public Optional<Mend> findBySn(String sn) {
        return Optional.fromNullable(mendDao.findBySn(sn));
    }

    @Override
    @OperationLog(desc = "查询报修信息")
    public List<Mend> retrieve(Map params)
    {
        List<Mend> mends = mendDao.getList(params);
        return mends;
    }

    @Override
    @OperationLog(desc = "查询报修数量")
    public Integer count(Map params) {
        return mendDao.count(params);
    }

    @Override
    @OperationLog(desc = "创建报修信息")
    public void create(Mend mend)
    {
        /*参数校验*/
        if (mend == null || mend.getMendType() == null || mend.getCustomer() == null ||
                mend.getRecvName() == null ||mend.getRecvName().trim().length() == 0||
                mend.getRecvPhone() == null ||mend.getRecvPhone().trim().length() == 0||
                mend.getRecvAddr() == null ||mend.getRecvAddr().getDetail().trim().length() == 0||
                mend.getDetail() == null ||mend.getDetail().trim().length() == 0
                )
        {
            throw new ServerSideBusinessException("报修信息不全，请补充报修信息！", HttpStatus.NOT_ACCEPTABLE);
        }


        /*报修类型信息校验*/
        if (mend.getMendType() != null || mend.getMendType().getCode() == null || mend.getMendType().getCode().trim().length() == 0)
        {
            MendType mendType = mendTypeDao.findByCode(mend.getMendType().getCode());
            if (mendType != null)
            {
                mend.setMendType(mendType);
            }
            else
            {
                throw new ServerSideBusinessException("报修类型信息错误！", HttpStatus.NOT_ACCEPTABLE);
            }
        }
        else
        {
            throw new ServerSideBusinessException("报修类型信息不能为空！", HttpStatus.NOT_ACCEPTABLE);
        }

         /*校验用户是否存在*/
        if (mend.getCustomer() == null || mend.getCustomer().getUserId() == null )
        {
            throw new ServerSideBusinessException("报修客户信息不全，请补充客户信息！", HttpStatus.NOT_ACCEPTABLE);
        }
        else
        {
            Customer customer = customerDao.findByUserId(mend.getCustomer().getUserId());
            if ( customer == null)
            {
                throw new ServerSideBusinessException("报修客户信息不正确，没有客户信息！", HttpStatus.NOT_ACCEPTABLE);
            }
            else
            {
                mend.setCustomer(customer);
            }
        }

        //生成定单编号
        Date curDate = new Date();
        String dateFmt =  new SimpleDateFormat("yyyyMMddHHmmssSSS").format(curDate);
        mend.setMendSn(dateFmt);

        //设置默认的订单状态
        mend.setProcessStatus(EProcessStatus.PTSuspending);
        mendDao.insert(mend);

    }

    @Override
    @OperationLog(desc = "修改报修单信息")
    public void update(String sn, Mend newMend)
    {
        /*参数校验*/
        if (sn == null || sn.trim().length() == 0 || newMend == null)
        {
            throw new ServerSideBusinessException("报修单信息不能为空！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*报修单是否存在*/
        Mend mend = findBySn(sn).get();
        if(mend == null)
        {
            throw new ServerSideBusinessException("报修单信息不存在！", HttpStatus.NOT_FOUND);
        }
        else
        {
            newMend.setId(mend.getId());
        }

        /*报修类型*/
        if (newMend.getMendType() != null && newMend.getMendType().getCode() != null && newMend.getMendType().getCode().trim().length() > 0)
        {
            MendType mendType = mendTypeDao.findByCode(newMend.getMendType().getCode());
            if (mendType != null)
            {
                newMend.setMendType(mendType);
            }
            else
            {
                throw new ServerSideBusinessException("报修类型信息不存在！", HttpStatus.NOT_ACCEPTABLE);
            }
        }
        else
        {
            newMend.setMendType(null);
        }
        /*如果要更新处理用户*/
        if (newMend.getDealedUser() == null || newMend.getDealedUser().getUserId() == null )
        {

        }
        else
        {
            SysUser sysUser = sysUserDao.findByUserId(newMend.getDealedUser().getUserId());
            if ( sysUser == null)
            {
                throw new ServerSideBusinessException("报修处理用户信息不正确，没有该用户信息！", HttpStatus.NOT_ACCEPTABLE);
            }
            else
            {
                newMend.setDealedUser(sysUser);
            }
        }

        /*如果要更新责任部门*/
        if (newMend.getDepartment() == null || newMend.getDepartment().getCode() == null )
        {

        }
        else
        {
            Department department = departmentDao.findByCode(newMend.getDepartment().getCode());
            if ( department == null)
            {
                throw new ServerSideBusinessException("报修处理责任部门信息不正确，没有该部门信息！", HttpStatus.NOT_ACCEPTABLE);
            }
            else
            {
                newMend.setDepartment(department);
            }
        }


        /*更新数据*/
        mendDao.update(newMend);
    }

    @Override
    @OperationLog(desc = "删除报修信息")
    public void deleteById(Integer id)
    {
        Mend mend = mendDao.findById(id);
        if (mend == null)
        {
            throw new ServerSideBusinessException("报修信息不存在！",HttpStatus.NOT_FOUND);
        }
        mendDao.delete(id);
    }

}

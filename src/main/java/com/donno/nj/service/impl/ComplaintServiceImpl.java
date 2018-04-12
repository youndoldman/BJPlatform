package com.donno.nj.service.impl;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.dao.*;
import com.donno.nj.domain.*;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.service.ComplaintService;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class ComplaintServiceImpl implements ComplaintService
{

    @Autowired
    private ComplaintDao complaintDao;

    @Autowired
    private ComplaintTypeDao complaintTypeDao;

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private SysUserDao sysUserDao;

    @Autowired
    private DepartmentDao departmentDao;




    @Override
    public Optional<Complaint> findBySn(String sn) {
        return Optional.fromNullable(complaintDao.findBySn(sn));
    }

    @Override
    @OperationLog(desc = "查询投诉信息")
    public List<Complaint> retrieve(Map params)
    {
        List<Complaint> complaints = complaintDao.getList(params);
        return complaints;
    }

    @Override
    @OperationLog(desc = "查询投诉数量")
    public Integer count(Map params) {
        return complaintDao.count(params);
    }

    @Override
    @OperationLog(desc = "创建投诉信息")
    public void create(Complaint complaint)
    {
        /*参数校验*/
        if (complaint == null || complaint.getcomplaintType() == null || complaint.getCustomer() == null ||
                complaint.getRecvName() == null ||complaint.getRecvName().trim().length() == 0||
                complaint.getRecvPhone() == null ||complaint.getRecvPhone().trim().length() == 0||
                complaint.getRecvAddr() == null ||complaint.getRecvAddr().getDetail().trim().length() == 0||
                complaint.getDetail() == null ||complaint.getDetail().trim().length() == 0
                )
        {
            throw new ServerSideBusinessException("投诉信息不全，请补充投诉信息！", HttpStatus.NOT_ACCEPTABLE);
        }


        /*投诉类型信息校验*/
        if (complaint.getcomplaintType() != null || complaint.getcomplaintType().getCode() == null || complaint.getcomplaintType().getCode().trim().length() == 0)
        {
            ComplaintType complaintType = complaintTypeDao.findByCode(complaint.getcomplaintType().getCode());
            if (complaintType != null)
            {
                complaint.setcomplaintType(complaintType);
            }
            else
            {
                throw new ServerSideBusinessException("投诉类型信息错误！", HttpStatus.NOT_ACCEPTABLE);
            }
        }
        else
        {
            throw new ServerSideBusinessException("投诉类型信息不能为空！", HttpStatus.NOT_ACCEPTABLE);
        }

         /*校验用户是否存在*/
        if (complaint.getCustomer() == null || complaint.getCustomer().getUserId() == null )
        {
            throw new ServerSideBusinessException("投诉客户信息不全，请补充客户信息！", HttpStatus.NOT_ACCEPTABLE);
        }
        else
        {
            Customer customer = customerDao.findByUserId(complaint.getCustomer().getUserId());
            if ( customer == null)
            {
                throw new ServerSideBusinessException("投诉客户信息不正确，没有客户信息！", HttpStatus.NOT_ACCEPTABLE);
            }
            else
            {
                complaint.setCustomer(customer);
            }
        }

        //生成定单编号
        Date curDate = new Date();
        String dateFmt =  new SimpleDateFormat("yyyyMMddHHmmssSSS").format(curDate);
        complaint.setcomplaintSn(dateFmt);

        //设置默认的订单状态
        complaint.setProcessStatus(EProcessStatus.PTSuspending);
        complaintDao.insert(complaint);

    }

    @Override
    @OperationLog(desc = "修改投诉单信息")
    public void update(String sn, Complaint newcomplaint)
    {
        /*参数校验*/
        if (sn == null || sn.trim().length() == 0 || newcomplaint == null)
        {
            throw new ServerSideBusinessException("投诉单信息不能为空！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*投诉单是否存在*/
        Complaint complaint = findBySn(sn).get();
        if(complaint == null)
        {
            throw new ServerSideBusinessException("投诉单信息不存在！", HttpStatus.NOT_FOUND);
        }
        else
        {
            newcomplaint.setId(complaint.getId());
        }

        /*投诉类型*/
        if (newcomplaint.getcomplaintType() != null && newcomplaint.getcomplaintType().getCode() != null && newcomplaint.getcomplaintType().getCode().trim().length() > 0)
        {
            ComplaintType complaintType = complaintTypeDao.findByCode(newcomplaint.getcomplaintType().getCode());
            if (complaintType != null)
            {
                newcomplaint.setcomplaintType(complaintType);
            }
            else
            {
                throw new ServerSideBusinessException("投诉类型信息不存在！", HttpStatus.NOT_ACCEPTABLE);
            }
        }
        else
        {
            newcomplaint.setcomplaintType(null);
        }
        /*如果要更新处理用户*/
        if (newcomplaint.getDealedUser() == null || newcomplaint.getDealedUser().getUserId() == null )
        {

        }
        else
        {
            SysUser sysUser = sysUserDao.findByUserId(newcomplaint.getDealedUser().getUserId());
            if ( sysUser == null)
            {
                throw new ServerSideBusinessException("投诉处理用户信息不正确，没有该用户信息！", HttpStatus.NOT_ACCEPTABLE);
            }
            else
            {
                newcomplaint.setDealedUser(sysUser);
            }
        }

        /*如果要更新责任部门*/
        if (newcomplaint.getDepartment() == null || newcomplaint.getDepartment().getCode() == null )
        {

        }
        else
        {
            Department department = departmentDao.findByCode(newcomplaint.getDepartment().getCode());
            if ( department == null)
            {
                throw new ServerSideBusinessException("投诉处理责任部门信息不正确，没有该部门信息！", HttpStatus.NOT_ACCEPTABLE);
            }
            else
            {
                newcomplaint.setDepartment(department);
            }
        }


        /*更新数据*/
        complaintDao.update(newcomplaint);
    }

    @Override
    @OperationLog(desc = "删除投诉信息")
    public void deleteById(Integer id)
    {
        Complaint complaint = complaintDao.findById(id);
        if (complaint == null)
        {
            throw new ServerSideBusinessException("投诉信息不存在！",HttpStatus.NOT_FOUND);
        }
        complaintDao.delete(id);
    }

}

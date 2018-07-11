package com.donno.nj.service.impl;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.dao.*;
import com.donno.nj.domain.*;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.service.SecurityService;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class SecurityServiceImpl implements SecurityService
{

    @Autowired
    private SecurityDao securityDao;

    @Autowired
    private SecurityTypeDao securityTypeDao;

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private SysUserDao sysUserDao;

    @Autowired
    private DepartmentDao departmentDao;




    @Override
    public Optional<Security> findBySn(String sn) {
        return Optional.fromNullable(securityDao.findBySn(sn));
    }

    @Override
    @OperationLog(desc = "查询安检信息")
    public List<Security> retrieve(Map params)
    {
        List<Security> securitys = securityDao.getList(params);
        return securitys;
    }

    @Override
    @OperationLog(desc = "查询安检数量")
    public Integer count(Map params) {
        return securityDao.count(params);
    }

    @Override
    @OperationLog(desc = "创建安检信息")
    public void create(Security security)
    {
        /*参数校验*/
        if (security == null || security.getsecurityType() == null || security.getCustomer() == null ||
                security.getRecvName() == null ||security.getRecvName().trim().length() == 0||
                security.getRecvPhone() == null ||security.getRecvPhone().trim().length() == 0||
                security.getRecvAddr() == null ||security.getRecvAddr().getDetail().trim().length() == 0||
                security.getDetail() == null ||security.getDetail().trim().length() == 0
                )
        {
            throw new ServerSideBusinessException("安检信息不全，请补充安检信息！", HttpStatus.NOT_ACCEPTABLE);
        }


        /*安检类型信息校验*/
        if (security.getsecurityType() != null || security.getsecurityType().getCode() == null || security.getsecurityType().getCode().trim().length() == 0)
        {
            SecurityType securityType = securityTypeDao.findByCode(security.getsecurityType().getCode());
            if (securityType != null)
            {
                security.setsecurityType(securityType);
            }
            else
            {
                throw new ServerSideBusinessException("安检类型信息错误！", HttpStatus.NOT_ACCEPTABLE);
            }
        }
        else
        {
            throw new ServerSideBusinessException("安检类型信息不能为空！", HttpStatus.NOT_ACCEPTABLE);
        }

         /*校验用户是否存在*/
        if (security.getCustomer() == null || security.getCustomer().getUserId() == null )
        {
            throw new ServerSideBusinessException("安检客户信息不全，请补充客户信息！", HttpStatus.NOT_ACCEPTABLE);
        }
        else
        {
            Customer customer = customerDao.findByCstUserId(security.getCustomer().getUserId());
            if ( customer == null)
            {
                throw new ServerSideBusinessException("安检客户信息不正确，没有客户信息！", HttpStatus.NOT_ACCEPTABLE);
            }
            else
            {
                security.setCustomer(customer);
            }
        }

        //生成定单编号
        Date curDate = new Date();
        String dateFmt =  new SimpleDateFormat("yyyyMMddHHmmssSSS").format(curDate);
        security.setsecuritySn(dateFmt);

        //设置默认的订单状态
        security.setProcessStatus(EProcessStatus.PTSuspending);
        securityDao.insert(security);

    }

    @Override
    @OperationLog(desc = "修改安检单信息")
    public void update(String sn, Security newsecurity)
    {
        /*参数校验*/
        if (sn == null || sn.trim().length() == 0 || newsecurity == null)
        {
            throw new ServerSideBusinessException("安检单信息不能为空！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*安检单是否存在*/
        Security security = findBySn(sn).get();
        if(security == null)
        {
            throw new ServerSideBusinessException("安检单信息不存在！", HttpStatus.NOT_FOUND);
        }
        else
        {
            newsecurity.setId(security.getId());
        }

        /*安检类型*/
        if (newsecurity.getsecurityType() != null && newsecurity.getsecurityType().getCode() != null && newsecurity.getsecurityType().getCode().trim().length() > 0)
        {
            SecurityType securityType = securityTypeDao.findByCode(newsecurity.getsecurityType().getCode());
            if (securityType != null)
            {
                newsecurity.setsecurityType(securityType);
            }
            else
            {
                throw new ServerSideBusinessException("安检类型信息不存在！", HttpStatus.NOT_ACCEPTABLE);
            }
        }
        else
        {
            newsecurity.setsecurityType(null);
        }
        /*如果要更新处理用户*/
        if (newsecurity.getDealedUser() == null || newsecurity.getDealedUser().getUserId() == null )
        {

        }
        else
        {
            SysUser sysUser = sysUserDao.findBySysUserId(newsecurity.getDealedUser().getUserId());
            if ( sysUser == null)
            {
                throw new ServerSideBusinessException("安检处理用户信息不正确，没有该用户信息！", HttpStatus.NOT_ACCEPTABLE);
            }
            else
            {
                newsecurity.setDealedUser(sysUser);
            }
        }

        /*如果要更新责任部门*/
        if (newsecurity.getDepartment() == null || newsecurity.getDepartment().getCode() == null )
        {

        }
        else
        {
            Department department = departmentDao.findByCode(newsecurity.getDepartment().getCode());
            if ( department == null)
            {
                throw new ServerSideBusinessException("安检处理责任部门信息不正确，没有该部门信息！", HttpStatus.NOT_ACCEPTABLE);
            }
            else
            {
                newsecurity.setDepartment(department);
            }
        }


        /*更新数据*/
        securityDao.update(newsecurity);
    }

    @Override
    @OperationLog(desc = "删除安检信息")
    public void deleteById(Integer id)
    {
        Security security = securityDao.findById(id);
        if (security == null)
        {
            throw new ServerSideBusinessException("安检信息不存在！",HttpStatus.NOT_FOUND);
        }
        securityDao.delete(id);
    }

}

package com.donno.nj.dao;


import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.CreditType;
import com.donno.nj.domain.CustomerCredit;
import com.donno.nj.domain.Ticket;
import org.apache.ibatis.annotations.Param;

public interface CustomerCreditDao extends BaseDao<CustomerCredit>
{
   CustomerCredit findByUserIdCreditType(@Param("userId") String userId, @Param("creditType") CreditType creditType);
    //CustomerCredit findByUserIdCreditType( String userId, CreditType creditType);
}

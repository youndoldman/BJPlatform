package com.donno.nj.service;

import com.donno.nj.domain.CstRefereeRel;
import com.donno.nj.domain.Customer;
import com.google.common.base.Optional;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
public interface CustomerService extends UserService
{
    List<Customer> retrieve(Map params);

    Integer count(Map params);

    void create(Customer user);

    void update(Integer id, Customer newUser);

    void deleteById(Integer id);

    Optional<Customer> findByCstUserId(String userId);

    List<String> getPhones(Map params);

	void updateLeakWarningTime(Map params);
    void addReferee(String customerId,String refereaeId);
    void removeReferee(String customerId,String refereeId);
    List<CstRefereeRel> getReferee(String customerId);

}

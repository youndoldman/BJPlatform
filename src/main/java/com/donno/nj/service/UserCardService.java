package com.donno.nj.service;

import com.donno.nj.domain.GasCynTray;
import com.donno.nj.domain.UserCard;
import com.google.common.base.Optional;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
public interface UserCardService
{
    List<UserCard> retrieve(Map params);

    Integer count(Map params);

    void create(UserCard userCard);

    void update(String number, UserCard newUserCard);

    void deleteById(Integer id);

    Optional<UserCard> findById(Integer id);


    void bind(String cardNumber, String userId);

    void unBind(String cardNumber, String userId);


}

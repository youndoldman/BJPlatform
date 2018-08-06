package com.donno.nj.service.impl;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.dao.*;
import com.donno.nj.domain.DeviceStatus;
import com.donno.nj.domain.GasCynTray;
import com.donno.nj.domain.User;
import com.donno.nj.domain.UserCard;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.service.GasCynTrayService;
import com.donno.nj.service.UserCardService;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class UserCardServiceImpl implements UserCardService
{

    @Autowired
    private UserCardDao userCardDao;

    @Autowired
    private CardUserRelDao cardUserRelDao;

    @Autowired
    private UserDao userDao;

    @Override
    @OperationLog(desc = "查询用户卡信息")
    public List<UserCard> retrieve(Map params)
    {
        return userCardDao.getList(params);
    }

    @Override
    @OperationLog(desc = "查询用户卡数量")
    public Integer count(Map params)
    {
        return  userCardDao.count(params);
    }

    public void checkCardNumber(String number)
    {
        if (number == null || number.trim().length() == 0)
        {
            throw new ServerSideBusinessException("缺少用户卡编号！", HttpStatus.NOT_ACCEPTABLE);
        }

        UserCard userCard = userCardDao.findByNumber(number);
        if (userCard != null)
        {
            String message = String.format("用户卡编号%s已经存在",number);
            throw new ServerSideBusinessException(message, HttpStatus.NOT_ACCEPTABLE);
        }
    }


    @Override
    @OperationLog(desc = "添加用户卡信息")
    public void create(UserCard userCard)
    {
        /*参数校验*/
        if (userCard == null)
        {
            throw new ServerSideBusinessException("用户卡信息不全，请补充信息！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*编号检查*/
        checkCardNumber(userCard.getNumber());

        userCardDao.insert(userCard);
    }

    @Override
    @OperationLog(desc = "修改用户卡信息")
    public void update(String number, UserCard  newUserCard)
    {
        /*参数校验*/
        if (newUserCard == null )
        {
            throw new ServerSideBusinessException("用户卡信息不能为空！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*用户卡是否存在*/
        UserCard userCard = userCardDao.findByNumber(number);
        if (userCard == null)
        {
            throw new ServerSideBusinessException("用户卡不存在！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*用户卡新编号是否存在*/
        if (newUserCard.getNumber() != null && newUserCard.getNumber().trim().length() > 0)
        {
            if (userCardDao.findByNumber(newUserCard.getNumber()) != null)
            {
                throw new ServerSideBusinessException("用户卡不存在！", HttpStatus.NOT_ACCEPTABLE);
            }
        }

        /*更新数据*/
        newUserCard.setId(userCard.getId());
        userCardDao.update(newUserCard);
    }


    @Override
    @OperationLog(desc = "建立绑定关系")
    public void bind(String cardNumber,String userId)
    {
        /*参数校验*/
        if (cardNumber == null || cardNumber.trim().length() == 0 )
        {
            throw new ServerSideBusinessException("用户卡信息不全，缺少用户卡卡号！", HttpStatus.NOT_ACCEPTABLE);
        }

        if (userId == null || userId.trim().length() == 0 )
        {
            throw new ServerSideBusinessException("客户信息不全，缺少客户ID！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*用户卡是否存在*/
        UserCard userCard = userCardDao.findByNumber(cardNumber);
        if ( userCard == null)
        {
            userCard = new UserCard();
            userCard.setNumber(cardNumber);
            userCard.setDeviceStatus(DeviceStatus.DevEnabled);
            userCardDao.insert(userCard);
        }

        /*如果用户卡已经被绑定了，提示应先解绑定*/
        if ( cardUserRelDao.findRelationByNumber(cardNumber) != null)
        {
            throw new ServerSideBusinessException("该用户卡已经绑定！", HttpStatus.CONFLICT);
        }

        /*客户是否存在*/
        User user = userDao.findByUserId(userId);
        if ( user == null)
        {
            throw new ServerSideBusinessException("客户不存在！", HttpStatus.NOT_FOUND);
        }

        cardUserRelDao.bind(userCard.getId(),user.getId());
    }

    @Override
    @OperationLog(desc = "解除绑定关系")
    public void unBind(String cardNumber,String userId)
    {
        /*参数校验*/
        if (cardNumber == null || cardNumber.trim().length() == 0 )
        {
            throw new ServerSideBusinessException("用户卡信息不全，缺少用户卡编号！", HttpStatus.NOT_ACCEPTABLE);
        }

        if (userId == null || userId.trim().length() == 0 )
        {
            throw new ServerSideBusinessException("客户信息不全，缺少客户ID！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*托盘是否存在*/
        UserCard userCard = userCardDao.findByNumber(cardNumber);
        if ( userCard == null)
        {
            throw new ServerSideBusinessException("用户卡信息不存在！", HttpStatus.NOT_FOUND);
        }

        /*客户是否存在*/
        User user = userDao.findByUserId(userId);
        if ( user == null)
        {
            throw new ServerSideBusinessException("该客户不存在！", HttpStatus.CONFLICT);
        }

        /*如果用户卡没有被绑定，提示应先绑定*/
        if ( cardUserRelDao.findRelationByNumber(cardNumber) == null)
        {
            throw new ServerSideBusinessException("该用户卡未绑定！", HttpStatus.NOT_FOUND);
        }

        cardUserRelDao.unBind(userCard.getId(),user.getId());
    }


    @Override
    @OperationLog(desc = "删除用户卡信息")
    public void deleteById(Integer id)
    {
        UserCard userCard = userCardDao.findById(id);
        if (userCard == null)
        {
            throw new ServerSideBusinessException("用户卡信息不存在！", HttpStatus.NOT_FOUND);
        }

         /*已经存在关联客户的托盘不允许修改*/
        if(cardUserRelDao.findRelationByNumber(userCard.getNumber()) != null)
        {
            throw new ServerSideBusinessException("用户卡已经绑定，请先解绑！", HttpStatus.NOT_FOUND);
        }

        userCardDao.delete(id);
    }

    @Override
    @OperationLog(desc = "查找用户卡信息")
    public Optional<UserCard> findById(Integer id)
    {
        return Optional.fromNullable(userCardDao.findById(id));
    }

}

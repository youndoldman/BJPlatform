package com.donno.nj.aspect.handler;

import com.donno.nj.exception.ForbiddenException;
import com.donno.nj.exception.UnauthorizedException;
import com.donno.nj.util.AppUtil;
import com.google.common.base.Optional;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import com.donno.nj.aspect.Auth;
import org.springframework.http.HttpStatus;
import com.donno.nj.domain.User;

/**
 * Created by Administrator on 2017\9\7 0007.
 */
@Aspect
public class AuthenticationAspect {
    @Around("@annotation(auth)")
    public Object UserAuthentication(ProceedingJoinPoint pjp, Auth auth) throws Throwable
    {
        Optional<User> curUser = AppUtil.getCurrentLoginUser();
        if(!curUser.isPresent()) throw new UnauthorizedException("用户未登录");

//        Boolean isForbidden = true;
//        String[] bizOperations = auth.allowedBizOp();
//        for(String bizOperation:bizOperations){
//            if(curUser.get().getRole().equals(bizOperation))
//            {
//                isForbidden = false;
//                break;
//            }
//        }
//        if(isForbidden){
//            throw new ForbiddenException("当前用户权限不足", HttpStatus.UNAUTHORIZED);
//        }

        return pjp.proceed();
    }

}

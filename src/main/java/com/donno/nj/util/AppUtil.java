package com.donno.nj.util;
import com.google.common.base.Optional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import  com.donno.nj.domain.User;


public class AppUtil
{
    public static final String SESSION_CUR_USER="curUser";

    public static HttpServletRequest getCurrentRequest()
    {
        return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public static HttpSession getCurrentSession()
    {
        HttpSession session = getCurrentRequest().getSession();
        session.setMaxInactiveInterval(3600);
        return session;
    }

    public static Optional<User> getCurrentLoginUser()
    {
        return Optional.fromNullable((User)getCurrentSession().getAttribute(SESSION_CUR_USER));
    }

    public static void setCurrentLoginUser(User user)
    {
        getCurrentSession().setAttribute(SESSION_CUR_USER, user);
    }

    public static void clearCurrentLoginUser()
    {
        setCurrentLoginUser(null);
        getCurrentSession().invalidate();
    }

}

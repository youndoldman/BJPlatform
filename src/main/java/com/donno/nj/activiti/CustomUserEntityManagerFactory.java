package com.donno.nj.activiti;
import org.activiti.engine.impl.interceptor.Session;
import org.activiti.engine.impl.interceptor.SessionFactory;
import org.activiti.engine.impl.persistence.entity.UserEntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.activiti.engine.impl.persistence.entity.UserIdentityManager;
/**
 * Created by Administrator on 2017\11\6 0006.
 */
public class CustomUserEntityManagerFactory implements SessionFactory {
    private UserEntityManager userEntityManager;

    @Autowired
    public void setUserEntityManager(UserEntityManager userEntityManager) {
        this.userEntityManager = userEntityManager;
    }

    public Class<?> getSessionType() {
        // 返回原始的UserManager类型
        return UserIdentityManager.class;
    }

    public Session openSession() {
        // 返回自定义的UserManager实例
        return userEntityManager;
    }
}
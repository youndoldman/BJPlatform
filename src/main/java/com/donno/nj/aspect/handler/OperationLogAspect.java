package com.donno.nj.aspect.handler;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.domain.sys.OperatorOpLog;
import com.google.common.collect.Lists;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class OperationLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(OperationLogAspect.class);

    @Around("@annotation(operationLog)")
    public Object operationLog(ProceedingJoinPoint pjp, OperationLog operationLog) throws Throwable {
        Object[] args = pjp.getArgs();
        String className = pjp.getSignature().getDeclaringType().getName();
        String methodName = pjp.getSignature().getName();

        try {
            String desc = operationLog.desc();
            String opParams = Lists.newArrayList(args).toString();
            logger.info(OperatorOpLog.log(className + ":"+ methodName , desc, opParams, "userId").toString());
        } catch (Exception e) {
            logger.error("fail to log operation", e);
        }
        return pjp.proceed();
    }
}

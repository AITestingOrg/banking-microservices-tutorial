package com.ultimatesoftware.banking.transactions.service.configuration;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class LoggingHandler {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void restController() {
    }

    @Pointcut("execution(public * *(..))")
    protected void publicOperation() {
    }

    @Around("restController() && publicOperation()")
    public Object logControllerMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        Object[] signatureArgs = joinPoint.getArgs();
        if (signatureArgs != null && signatureArgs.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < joinPoint.getArgs().length; i++) {
                sb.append(signatureArgs[i]);
                if (i < joinPoint.getArgs().length - 1) {
                    sb.append(", ");
                }
            }
            log.info("\n{}.{} \nwas called with args:\n{}",
                     joinPoint.getTarget().getClass().getCanonicalName(), method.getName(), sb.toString());
        }

        //start method execution
        Object result = joinPoint.proceed();

        long executionTime = System.currentTimeMillis() - startTime;
        log.info("{} executed in {} ms", signature, executionTime);

        return result;
    }
}

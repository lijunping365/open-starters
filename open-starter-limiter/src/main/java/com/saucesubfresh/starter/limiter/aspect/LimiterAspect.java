package com.saucesubfresh.starter.limiter.aspect;

import com.saucesubfresh.starter.limiter.annotation.Limiter;
import com.saucesubfresh.starter.limiter.process.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author lijunping on 2022/8/25
 */
@Slf4j
@Aspect
public class LimiterAspect {

    private final RateLimiter rateLimiter;

    public LimiterAspect(RateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    @Pointcut("@annotation(com.saucesubfresh.starter.limiter.annotation.Limiter)")
    public void dsPointcut() {}

    @Around("dsPointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable{
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        String lockName = keyGenerator.generate(method, joinPoint.getArgs());
        return limit(joinPoint, method, lockName);
    }

    private Object limit(ProceedingJoinPoint pjp, Method method, final String lockName) throws Throwable{
        Limiter annotation = method.getAnnotation(Limiter.class);
        boolean tryLock = annotation.tryLock();
        boolean fairLock = annotation.fairLock();
        long leaseTime = annotation.leaseTime();
        long waitTime = annotation.waitTime();
        TimeUnit timeUnit = annotation.timeUnit();
        if (tryLock) {
            return rateLimiter.tryLock(()-> proceed(pjp), lockName, waitTime, leaseTime, timeUnit, fairLock);
        } else {
            return rateLimiter.lock(()-> proceed(pjp), lockName, leaseTime, timeUnit, fairLock);
        }
    }

    private Object proceed(ProceedingJoinPoint pjp) {
        try {
            return pjp.proceed();
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }
}

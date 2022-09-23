package com.saucesubfresh.starter.limiter.aspect;

import com.saucesubfresh.starter.limiter.annotation.RateLimit;
import com.saucesubfresh.starter.limiter.generator.KeyGenerator;
import com.saucesubfresh.starter.limiter.processor.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * @author lijunping on 2022/8/25
 */
@Slf4j
@Aspect
public class LimiterAspect {

    private final RateLimiter rateLimiter;
    private final KeyGenerator keyGenerator;

    public LimiterAspect(RateLimiter rateLimiter, KeyGenerator keyGenerator) {
        this.rateLimiter = rateLimiter;
        this.keyGenerator = keyGenerator;
    }

    @Pointcut("@annotation(com.saucesubfresh.starter.limiter.annotation.RateLimit)")
    public void dsPointcut() {}

    @Around("dsPointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable{
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        String limitKey = keyGenerator.generate(method, joinPoint.getArgs());
        return tryAcquire(joinPoint, method, limitKey);
    }

    private Object tryAcquire(ProceedingJoinPoint pjp, Method method, final String limitKey) throws Throwable{
        RateLimit annotation = method.getAnnotation(RateLimit.class);
        final int rate = annotation.replenishRate();
        final int capacity = annotation.burstCapacity();
        final int permits = annotation.requestedTokens();
        return rateLimiter.tryAcquire(()-> proceed(pjp), limitKey, rate, capacity, permits);
    }

    private Object proceed(ProceedingJoinPoint pjp) {
        try {
            return pjp.proceed();
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }
}

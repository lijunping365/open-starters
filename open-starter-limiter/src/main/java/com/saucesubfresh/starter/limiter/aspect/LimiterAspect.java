package com.saucesubfresh.starter.limiter.aspect;

import com.saucesubfresh.starter.limiter.annotation.Limiter;
import com.saucesubfresh.starter.limiter.generator.KeyGenerator;
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
    private final KeyGenerator keyGenerator;

    public LimiterAspect(RateLimiter rateLimiter, KeyGenerator keyGenerator) {
        this.rateLimiter = rateLimiter;
        this.keyGenerator = keyGenerator;
    }

    @Pointcut("@annotation(com.saucesubfresh.starter.limiter.annotation.Limiter)")
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
        Limiter annotation = method.getAnnotation(Limiter.class);
        final double rate = annotation.rate();
        final int permits = annotation.permits();
        final int capacity = annotation.capacity();
        return rateLimiter.tryAcquire(()-> proceed(pjp), limitKey, capacity, permits, rate);
    }

    private Object proceed(ProceedingJoinPoint pjp) {
        try {
            return pjp.proceed();
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }
}

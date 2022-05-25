package com.saucesubfresh.starter.cache.aspect;

import com.saucesubfresh.starter.cache.annotation.CacheEvict;
import com.saucesubfresh.starter.cache.annotation.Cacheable;
import com.saucesubfresh.starter.cache.core.Cache;
import com.saucesubfresh.starter.cache.generator.KeyGenerator;
import com.saucesubfresh.starter.cache.handler.CacheHandler;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * @author lijunping on 2022/5/25
 */
@Aspect
public class CacheAspect {

    private final CacheHandler cacheHandler;

    public CacheAspect(CacheHandler cacheHandler) {
        this.cacheHandler = cacheHandler;
    }

    @Around("@annotation(cacheable)")
    public Object cacheClear(ProceedingJoinPoint joinPoint, Cacheable cacheable) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Class<?> methodReturnType = method.getReturnType();
        return cacheHandler.handlerCacheable(cacheable, methodReturnType, joinPoint.getArgs(), ()->{
            return joinPoint.proceed(joinPoint.getArgs());
        });
    }

    @Around("@annotation(cacheEvict)")
    public Object cacheClear(ProceedingJoinPoint joinPoint, CacheEvict cacheEvict) throws Throwable {
        Object result = joinPoint.proceed(joinPoint.getArgs());
        cacheHandler.handlerCacheEvict(cacheEvict, joinPoint.getArgs());
        return result;

    }
}

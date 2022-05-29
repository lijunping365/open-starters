package com.saucesubfresh.starter.cache.aspect;

import com.saucesubfresh.starter.cache.annotation.CacheEvict;
import com.saucesubfresh.starter.cache.annotation.Cacheable;
import com.saucesubfresh.starter.cache.handler.CacheAnnotationHandler;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * @author lijunping on 2022/5/25
 */
@Aspect
public class CacheAspect {

    private final CacheAnnotationHandler cacheAnnotationHandler;

    public CacheAspect(CacheAnnotationHandler cacheAnnotationHandler) {
        this.cacheAnnotationHandler = cacheAnnotationHandler;
    }

    @Pointcut("@annotation(com.saucesubfresh.starter.cache.annotation.Cacheable)")
    public void doCacheable() {}

    @Pointcut("@annotation(com.saucesubfresh.starter.cache.annotation.CacheEvict)")
    public void doCacheEvict() {}

    @Around("doCacheable()")
    public Object cacheable(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Cacheable cacheable = method.getAnnotation(Cacheable.class);
        Class<?> methodReturnType = method.getReturnType();
        return cacheAnnotationHandler.handlerCacheable(cacheable, methodReturnType, joinPoint.getArgs(), ()->{
            return joinPoint.proceed(joinPoint.getArgs());
        });
    }

    @Around("doCacheEvict()")
    public Object cacheClear(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Object result = joinPoint.proceed(joinPoint.getArgs());
        CacheEvict cacheable = method.getAnnotation(CacheEvict.class);
        cacheAnnotationHandler.handlerCacheEvict(cacheable, joinPoint.getArgs());
        return result;
    }
}

package com.saucesubfresh.starter.cache.aspect;

import com.saucesubfresh.starter.cache.annotation.OpenCacheClear;
import com.saucesubfresh.starter.cache.annotation.OpenCacheEvict;
import com.saucesubfresh.starter.cache.annotation.OpenCacheable;
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

    @Pointcut("@annotation(com.saucesubfresh.starter.cache.annotation.OpenCacheable)")
    public void doCacheable() {}

    @Pointcut("@annotation(com.saucesubfresh.starter.cache.annotation.OpenCacheEvict)")
    public void doCacheEvict() {}

    @Pointcut("@annotation(com.saucesubfresh.starter.cache.annotation.OpenCacheClear)")
    public void doCacheClear() {}

    @Around("doCacheable()")
    public Object cacheable(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        OpenCacheable openCacheable = method.getAnnotation(OpenCacheable.class);
        Class<?> methodReturnType = method.getReturnType();
        return cacheAnnotationHandler.handlerCacheable(openCacheable, methodReturnType, joinPoint.getArgs(), ()->{
            return joinPoint.proceed(joinPoint.getArgs());
        });
    }

    @Around("doCacheEvict()")
    public Object cacheEvict(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Object result = joinPoint.proceed(joinPoint.getArgs());
        OpenCacheEvict cacheable = method.getAnnotation(OpenCacheEvict.class);
        cacheAnnotationHandler.handlerCacheEvict(cacheable, joinPoint.getArgs());
        return result;
    }

    @Around("doCacheClear()")
    public Object cacheClear(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Object result = joinPoint.proceed(joinPoint.getArgs());
        OpenCacheClear cacheClear = method.getAnnotation(OpenCacheClear.class);
        cacheAnnotationHandler.handlerCacheClear(cacheClear, joinPoint.getArgs());
        return result;
    }
}

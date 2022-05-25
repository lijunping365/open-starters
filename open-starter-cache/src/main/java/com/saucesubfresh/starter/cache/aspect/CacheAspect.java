package com.saucesubfresh.starter.cache.aspect;

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

    private final KeyGenerator keyGenerator;
    private final CacheHandler cacheHandler;

    public CacheAspect(KeyGenerator keyGenerator, CacheHandler cacheHandler) {
        this.keyGenerator = keyGenerator;
        this.cacheHandler = cacheHandler;
    }

    @Around("@annotation(cacheable)")
    public Object cacheClear(ProceedingJoinPoint joinPoint, Cacheable cacheable) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Class<?> methodReturnType = method.getReturnType();
        return cacheHandler.handlerCacheable(cacheable, methodReturnType, joinPoint.getArgs(), ()->{
            try {
                return joinPoint.proceed(joinPoint.getArgs());
            } catch (Throwable e) {
                e.printStackTrace();
            }
        });
    }
}

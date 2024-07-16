/*
 * Copyright © 2022 organization openbytecode
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.openbytecode.starter.cache.aspect;

import com.openbytecode.starter.cache.annotation.OpenCacheClear;
import com.openbytecode.starter.cache.annotation.OpenCacheEvict;
import com.openbytecode.starter.cache.annotation.OpenCachePut;
import com.openbytecode.starter.cache.annotation.OpenCacheable;
import com.openbytecode.starter.cache.generator.KeyGenerator;
import com.openbytecode.starter.cache.processor.CacheProcessor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * @author lijunping
 */
@Aspect
public class CacheAspect {

    private final KeyGenerator keyGenerator;
    private final CacheProcessor cacheProcessor;

    public CacheAspect(KeyGenerator keyGenerator, CacheProcessor cacheProcessor) {
        this.keyGenerator = keyGenerator;
        this.cacheProcessor = cacheProcessor;
    }

    @Pointcut("@annotation(com.openbytecode.starter.cache.annotation.OpenCacheable)")
    public void doCacheable() {}

    @Pointcut("@annotation(com.openbytecode.starter.cache.annotation.OpenCacheEvict)")
    public void doCacheEvict() {}

    @Pointcut("@annotation(com.openbytecode.starter.cache.annotation.OpenCacheClear)")
    public void doCacheClear() {}

    @Pointcut("@annotation(com.openbytecode.starter.cache.annotation.OpenCachePut)")
    public void doCachePut() {}

    @Around("doCacheable()")
    public Object cacheable(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        OpenCacheable openCacheable = method.getAnnotation(OpenCacheable.class);
        final String cacheName = openCacheable.cacheName();
        String cacheKey = keyGenerator.generate(openCacheable.key(), method, joinPoint.getArgs());
        return cacheProcessor.handlerCacheable(()-> proceed(joinPoint), cacheName, cacheKey, method.getReturnType());
    }

    @Around("doCacheEvict()")
    public Object cacheEvict(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Object result = proceed(joinPoint);
        OpenCacheEvict cacheEvict = method.getAnnotation(OpenCacheEvict.class);
        final String cacheName = cacheEvict.cacheName();
        String cacheKey = keyGenerator.generate(cacheEvict.key(), method, joinPoint.getArgs());
        cacheProcessor.handlerCacheEvict(cacheName, cacheKey);
        return result;
    }

    @Around("doCacheClear()")
    public Object cacheClear(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Object result = proceed(joinPoint);
        OpenCacheClear cacheClear = method.getAnnotation(OpenCacheClear.class);
        cacheProcessor.handlerCacheClear(cacheClear.cacheName());
        return result;
    }

    @Around("doCachePut()")
    public Object cachePut(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Object result = proceed(joinPoint);
        OpenCachePut cachePut = method.getAnnotation(OpenCachePut.class);
        final String cacheName = cachePut.cacheName();
        String cacheKey = keyGenerator.generate(cachePut.key(), method, joinPoint.getArgs());
        cacheProcessor.handlerCachePut(cacheName, cacheKey, result);
        return result;
    }

    private Object proceed(ProceedingJoinPoint pjp) {
        try {
            return pjp.proceed();
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }
}

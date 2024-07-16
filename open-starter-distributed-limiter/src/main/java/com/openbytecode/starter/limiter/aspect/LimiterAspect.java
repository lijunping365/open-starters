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
package com.openbytecode.starter.limiter.aspect;

import com.openbytecode.starter.limiter.processor.RateLimiter;
import com.openbytecode.starter.limiter.annotation.RateLimit;
import com.openbytecode.starter.limiter.generator.KeyGenerator;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * @author lijunping
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

    @Pointcut("@annotation(com.openbytecode.starter.limiter.annotation.RateLimit)")
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

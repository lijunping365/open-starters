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
package com.openbytecode.starter.lock.aspect;

import com.openbytecode.starter.lock.annotation.Lock;
import com.openbytecode.starter.lock.generator.KeyGenerator;
import com.openbytecode.starter.lock.processor.DistributedLockProcessor;
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
 * 用于某些方法加分布式的同步锁
 *
 * @author lijunping
 */
@Aspect
@Slf4j
public class DistributedLockAspect {

  private final DistributedLockProcessor lockProcessor;
  private final KeyGenerator keyGenerator;

  public DistributedLockAspect(DistributedLockProcessor lockProcessor, KeyGenerator keyGenerator) {
    this.lockProcessor = lockProcessor;
    this.keyGenerator = keyGenerator;
  }

  @Pointcut("@annotation(com.openbytecode.starter.lock.annotation.Lock)")
  public void dsPointcut() {}

  @Around("dsPointcut()")
  public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable{
    Signature signature = joinPoint.getSignature();
    MethodSignature methodSignature = (MethodSignature) signature;
    Method method = methodSignature.getMethod();
    String lockName = keyGenerator.generate(method, joinPoint.getArgs());
    return lock(joinPoint, method, lockName);
  }

  private Object lock(ProceedingJoinPoint pjp, Method method, final String lockName) throws Throwable{
    Lock annotation = method.getAnnotation(Lock.class);
    boolean tryLock = annotation.tryLock();
    boolean fairLock = annotation.fairLock();
    long leaseTime = annotation.leaseTime();
    long waitTime = annotation.waitTime();
    TimeUnit timeUnit = annotation.timeUnit();
    if (tryLock) {
      return lockProcessor.tryLock(()-> proceed(pjp), lockName, waitTime, leaseTime, timeUnit, fairLock);
    } else {
      return lockProcessor.lock(()-> proceed(pjp), lockName, leaseTime, timeUnit, fairLock);
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

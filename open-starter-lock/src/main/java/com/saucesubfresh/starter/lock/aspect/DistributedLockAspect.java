package com.saucesubfresh.starter.lock.aspect;

import com.saucesubfresh.starter.lock.annotation.DistributedLock;
import com.saucesubfresh.starter.lock.processor.DistributedLockProcessor;
import com.saucesubfresh.starter.lock.generator.KeyGenerator;
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
 * @author: 李俊平
 * @Date: 2020-11-11 10:18
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

  @Pointcut("@annotation(com.saucesubfresh.starter.lock.annotation.DistributedLock)")
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
    DistributedLock annotation = method.getAnnotation(DistributedLock.class);
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

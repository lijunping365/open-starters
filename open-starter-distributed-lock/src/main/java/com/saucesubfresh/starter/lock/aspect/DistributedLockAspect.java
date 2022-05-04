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
  public Object doAround(ProceedingJoinPoint joinPoint) {
    Signature signature = joinPoint.getSignature();
    MethodSignature methodSignature = (MethodSignature) signature;
    Method method = methodSignature.getMethod();
    String lockName = keyGenerator.generate(method, joinPoint.getArgs());
    return lock(joinPoint, method, lockName);
  }

  private Object lock(ProceedingJoinPoint pjp, Method method, final String lockName) {
    DistributedLock annotation = method.getAnnotation(DistributedLock.class);
    boolean tryLock = annotation.tryLock();
    if (tryLock) {
      return tryLock(pjp, annotation, lockName);
    } else {
      return lock(pjp, annotation, lockName);
    }
  }

  private Object lock(ProceedingJoinPoint pjp, DistributedLock annotation, String lockName) {
    try {
      return lockProcessor.lock(()-> proceed(pjp), lockName, annotation.leaseTime(), annotation.timeUnit(), annotation.fairLock());
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  private Object tryLock(ProceedingJoinPoint pjp, DistributedLock annotation, String lockName) {
    try {
      return lockProcessor.tryLock(()-> proceed(pjp), lockName,annotation.waitTime(), annotation.leaseTime(), annotation.timeUnit(), annotation.fairLock());
    } catch (Exception e) {
      e.printStackTrace();
      return null;
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

package com.saucesubfresh.starter.datasource.aspect;

import com.saucesubfresh.starter.datasource.annotation.SwitchDs;
import com.saucesubfresh.starter.datasource.support.DataSourceContextHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Method;

/**
 * @author lijunping on 2021/5/17
 */
@Aspect
@Order(-10)
public class SwitchAspect {

  @Pointcut("@annotation(com.saucesubfresh.starter.datasource.annotation.SwitchDs)")
  public void dsPointcut(){};


  @Around("dsPointcut()")
  public Object around(ProceedingJoinPoint joinPoint){
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    Method method = signature.getMethod();
    SwitchDs annotation = method.getAnnotation(SwitchDs.class);

    if(annotation != null){//定义了切点，则取切点上定义的数据源进行切换
      DataSourceContextHolder.setDbType(annotation.dsName());
    }

    Object proceed = null;
    try {
      proceed = joinPoint.proceed();
    } catch (Throwable throwable) {
      throwable.printStackTrace();
    }finally {
      DataSourceContextHolder.clearDbType();
    }
    return proceed;
  }
}

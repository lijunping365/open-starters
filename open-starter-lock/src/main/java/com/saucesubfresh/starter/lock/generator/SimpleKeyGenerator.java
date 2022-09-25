package com.saucesubfresh.starter.lock.generator;


import com.saucesubfresh.starter.lock.annotation.Lock;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;

/**
 * @author lijunping on 2021/12/1
 */
public class SimpleKeyGenerator implements KeyGenerator{

  @Override
  public String generate(Method method, Object[] args) {
    Lock annotation = method.getAnnotation(Lock.class);
    String lockName = annotation.lockName();
    return StringUtils.isNotBlank(lockName) ? lockName : method.getName();
  }

}

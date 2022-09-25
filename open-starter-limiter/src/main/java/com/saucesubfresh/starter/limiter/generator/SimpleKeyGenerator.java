package com.saucesubfresh.starter.limiter.generator;


import com.saucesubfresh.starter.limiter.annotation.RateLimit;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;

/**
 * @author lijunping on 2021/12/1
 */
public class SimpleKeyGenerator implements KeyGenerator{

  @Override
  public String generate(Method method, Object[] args) {
      RateLimit annotation = method.getAnnotation(RateLimit.class);
      String key = annotation.limitKey();
      return StringUtils.isNotBlank(key) ? key : method.getName();
  }
}

package com.saucesubfresh.starter.security.annotation;

import java.lang.annotation.*;

/**
 * 权限控制注解
 *
 * @author: 李俊平
 * @Date: 2020-11-19 12:56
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PreAuthorization {
  
}

package com.saucesubfresh.starter.datasource.annotation;


import java.lang.annotation.*;

/**
 * @author lijunping on 2021/5/17
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SwitchDs {

  /**
   * 数据源名称：dataSourceName
   */
  String dsName();

}

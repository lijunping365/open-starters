package com.saucesubfresh.starter.datasource.support;

/**
 * 用于获取当前线程数据源并设置
 * @author lijunping on 2021/5/17
 */
public class DataSourceContextHolder {

  private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

  public static void setDbType(String dbType) {
    contextHolder.set(dbType);
  }

  public static String getDbType() {
    return contextHolder.get();
  }

  public static void clearDbType() {
    contextHolder.remove();
  }

}

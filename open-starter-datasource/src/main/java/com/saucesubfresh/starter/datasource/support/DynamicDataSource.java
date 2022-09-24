package com.saucesubfresh.starter.datasource.support;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.Map;

/**
 * 将数据源注册到AbstractRoutingDataSource
 * 注意 注入读写数据源时要使用@qualifier注解 指定注入数据源 不然会报错 同时类上要加上@primary 首选加载此类
 * @author lijunping on 2021/5/17
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

  private final Map<Object, Object> dataSourceMap;

  public DynamicDataSource(Map<Object, Object> dataSourceMap) {
    this.dataSourceMap = dataSourceMap;
  }

  @Override
  protected Object determineCurrentLookupKey() {
    String dbType = DataSourceContextHolder.getDbType();
    System.out.println("当前数据源类型是："+dbType);
    return dbType;
  }

  /**
   * 配置数据源信息
   */
  @Override
  public void afterPropertiesSet() {
    setTargetDataSources(dataSourceMap);
    setDefaultTargetDataSource(masterDataSource);
    super.afterPropertiesSet();
  }

}

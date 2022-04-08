package com.saucesubfresh.starter.executor.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 任务执行配置类
 */
@Data
@ConfigurationProperties("com.pro.crawler.thread")
public class TaskExecutorProperties {

  /**
   * 核心线程数
   */
  private Integer corePoolSize = 5;

  /**
   * 最大线程数
   */
  private Integer maximumPoolSize = 10;

  /**
   * 线程空闲时间
   */
  private Long keepAliveTime = 60L;

  /**
   * 队列大小
   */
  private Integer queueCapacity = 100;

  /**
   * 任务执行线程前缀
   */
  private String prefix = "crawler-task";

}

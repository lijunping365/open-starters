package com.saucesubfresh.starter.schedule.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: 李俊平
 * @Date: 2022-07-03 08:39
 */
@Data
@ConfigurationProperties(prefix = "com.saucesubfresh.cache")
public class ScheduleProperties {

    /**
     * 任务队列名称 （zset）
     */
    private String taskQueueName = "schedule:task:queue";

    /**
     * 核心线程数
     */
    private Integer corePoolSize = Runtime.getRuntime().availableProcessors() * 2;

    /**
     * 最大线程数
     */
    private Integer maximumPoolSize = Runtime.getRuntime().availableProcessors() * 4;

    /**
     * 线程空闲时间
     */
    private Long keepAliveTime = 60L;

    /**
     * 队列大小
     */
    private Integer queueCapacity = 1000;

    /**
     * 任务执行线程前缀
     */
    private String prefix = "schedule-task";
}

package com.saucesubfresh.starter.schedule.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: 李俊平
 * @Date: 2022-07-03 08:39
 */
@Data
@ConfigurationProperties(prefix = "com.saucesubfresh.schedule")
public class ScheduleProperties {

    /**
     * 任务池名称 （hash）
     */
    private String taskPoolName = "schedule:task:pool";

    /**
     * 任务队列名称 （zset）
     */
    private String taskQueueName = "schedule:task:queue";
}

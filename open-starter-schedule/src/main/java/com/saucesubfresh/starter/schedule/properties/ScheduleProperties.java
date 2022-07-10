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
}

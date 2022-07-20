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
     * 时间轮槽数
     */
    private long tickDuration = 60;

    /**
     * 任务池名称 （hash）
     */
    private String taskPoolName = "schedule:task:pool";

    /**
     * 分布式锁名称
     */
    private String lockName = "schedule:lock";
}

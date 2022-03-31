package com.lightcode.starter.schedule.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lijunping on 2022/1/20
 */
@Data
public class ScheduleTask implements Serializable {
    private static final long serialVersionUID = -1936628567018899417L;
    /**
     * 任务 id
     */
    private Long taskId;

    /**
     * cron 表达式
     */
    private String cronExpression;
}

package com.lightcode.starter.schedule.executor;

import java.util.List;

/**
 * 调度任务执行器
 * @author lijunping on 2022/1/20
 */
public interface ScheduleTaskExecutor {

    void execute(List<Long> taskIds);
}

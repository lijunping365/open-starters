package com.saucesubfresh.starter.schedule.executor;

import com.saucesubfresh.starter.schedule.domain.ScheduleTask;

import java.util.List;

/**
 * 调度任务执行器
 * @author lijunping on 2022/1/20
 */
public interface ScheduleTaskExecutor {

    void execute(List<Long> taskList);
}

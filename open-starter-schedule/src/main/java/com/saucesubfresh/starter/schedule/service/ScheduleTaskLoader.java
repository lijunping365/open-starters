package com.saucesubfresh.starter.schedule.service;

import com.saucesubfresh.starter.schedule.domain.ScheduleTask;

import java.util.List;

/**
 * @author lijunping on 2022/7/6
 */
public interface ScheduleTaskLoader {

    /**
     * 加载调度任务
     * @return
     */
    List<ScheduleTask> loadScheduleTask();
}

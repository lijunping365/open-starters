package com.saucesubfresh.starter.schedule.core;


import com.saucesubfresh.starter.schedule.domain.ScheduleTask;

import java.util.List;

/**
 * @author lijunping on 2022/1/20
 */
public interface ScheduleTaskManage {

    /**
     * 添加任务
     * @param scheduleTask
     * @return
     */
    void addScheduleTask(ScheduleTask scheduleTask);

    /**
     * 移除任务
     * @param taskId
     * @return
     */
    void removeScheduleTask(Long taskId);

    /**
     * 获取调度任务
     * @return
     */
    List<Long> getScheduleTaskList();
}

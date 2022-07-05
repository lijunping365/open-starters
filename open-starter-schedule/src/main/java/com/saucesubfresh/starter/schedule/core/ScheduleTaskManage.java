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
     * 获取即将被调度的任务
     * @return 调度任务的 id 集合
     */
    List<Long> takeScheduleTask();

    /**
     * 获取任务池中存在的全部任务
     * @return 任务池中存在的全部任务
     */
    List<ScheduleTask> getScheduleTask();
}

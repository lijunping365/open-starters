package com.saucesubfresh.starter.schedule.manage;


import com.saucesubfresh.starter.schedule.domain.ScheduleTask;

import java.util.List;

/**
 * @author lijunping on 2022/1/20
 */
public interface ScheduleTaskManage {

    /**
     * 获取即将被调度的任务
     * @return 调度任务集合
     */
    ScheduleTask get(Long taskId);

    /**
     * 获取任务池中存在的全部任务
     * @return 任务池中存在的全部任务
     */
    List<ScheduleTask> getAll();

    /**
     * 添加任务
     * @param scheduleTask
     * @return
     */
    void add(ScheduleTask scheduleTask);

    /**
     * 移除任务
     * @param scheduleTask
     * @return
     */
    void remove(ScheduleTask scheduleTask);
}

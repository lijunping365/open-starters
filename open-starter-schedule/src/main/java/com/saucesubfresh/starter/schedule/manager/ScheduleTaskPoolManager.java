package com.saucesubfresh.starter.schedule.manager;


import com.saucesubfresh.starter.schedule.domain.ScheduleTask;

import java.util.Collection;
import java.util.List;

/**
 * 任务池实现
 * @author lijunping on 2022/1/20
 */
public interface ScheduleTaskPoolManager {

    /**
     * 批量添加任务
     * @param task
     */
    void addAll(List<ScheduleTask> task);

    /**
     * 获取任务池中存在的全部任务
     * @return 任务池中存在的全部任务
     */
    Collection<ScheduleTask> getAll();

    /**
     * 获取即将被调度的任务
     * @return 调度任务集合
     */
    ScheduleTask get(Long taskId);

    /**
     * 添加任务
     * @param task
     */
    void add(ScheduleTask task);

    /**
     * 移除任务
     * @param taskId
     */
    void remove(Long taskId);
}

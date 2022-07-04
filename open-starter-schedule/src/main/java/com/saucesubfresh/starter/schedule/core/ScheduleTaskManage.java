package com.saucesubfresh.starter.schedule.core;


import com.saucesubfresh.starter.schedule.domain.ScheduleTask;
import com.saucesubfresh.starter.schedule.exception.ScheduleException;

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
    void addScheduleTask(ScheduleTask scheduleTask) throws ScheduleException;

    /**
     * 移除任务
     * @param taskId
     * @return
     */
    void removeScheduleTask(Long taskId) throws ScheduleException;

    /**
     * 获取调度任务
     * @return
     */
    List<Long> getScheduleTaskList() throws ScheduleException;
}

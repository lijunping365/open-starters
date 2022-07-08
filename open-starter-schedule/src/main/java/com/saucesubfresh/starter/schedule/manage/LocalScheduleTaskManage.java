package com.saucesubfresh.starter.schedule.manage;

import com.saucesubfresh.starter.schedule.domain.ScheduleTask;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 适用于单节点
 * @author lijunping on 2022/7/8
 */
public class LocalScheduleTaskManage implements ScheduleTaskManage{

    private final ConcurrentMap<Long, ScheduleTask> taskMap = new ConcurrentHashMap<>(16);

    @Override
    public ScheduleTask get(Long taskId) {
        return taskMap.get(taskId);
    }

    @Override
    public List<ScheduleTask> getAll() {
        return (List<ScheduleTask>) taskMap.values();
    }

    @Override
    public void add(ScheduleTask scheduleTask) {
        taskMap.put(scheduleTask.getTaskId(), scheduleTask);
    }

    @Override
    public void remove(ScheduleTask scheduleTask) {
        taskMap.remove(scheduleTask.getTaskId());
    }
}

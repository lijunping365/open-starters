package com.saucesubfresh.starter.schedule.manager;

import com.saucesubfresh.starter.schedule.domain.ScheduleTask;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * 适用于单节点
 * @author lijunping on 2022/7/8
 */
public class LocalScheduleTaskPoolManager extends AbstractScheduleTaskPoolManager {

    private final ConcurrentMap<Long, ScheduleTask> taskMap = new ConcurrentHashMap<>(16);

    @Override
    public void addAll(List<ScheduleTask> taskList) {
        if (CollectionUtils.isEmpty(taskList)){
            return;
        }
        for (ScheduleTask scheduleTask : taskList) {
            setNextTime(scheduleTask);
        }
        Map<Long, ScheduleTask> taskMap = taskList.stream().collect(Collectors.toMap(ScheduleTask::getTaskId, o -> o));
        this.taskMap.putAll(taskMap);
    }

    @Override
    public Collection<ScheduleTask> getAll() {
        return taskMap.values();
    }

    @Override
    public ScheduleTask get(Long taskId) {
        return taskMap.get(taskId);
    }

    @Override
    public void add(ScheduleTask scheduleTask) {
        setNextTime(scheduleTask);
        taskMap.put(scheduleTask.getTaskId(), scheduleTask);
    }

    @Override
    public void remove(Long taskId) {
        taskMap.remove(taskId);
    }
}

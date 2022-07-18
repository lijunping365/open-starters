package com.saucesubfresh.starter.schedule.manager;

import com.saucesubfresh.starter.schedule.domain.ScheduleTask;
import com.saucesubfresh.starter.schedule.exception.ScheduleException;
import com.saucesubfresh.starter.schedule.properties.ScheduleProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 适用于多节点
 * @author lijunping on 2022/1/20
 */
@Slf4j
public class RedisScheduleTaskPoolManager implements ScheduleTaskPoolManager {

    private final String taskPoolName;
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisScheduleTaskPoolManager(ScheduleProperties scheduleProperties,
                                        RedisTemplate<String, Object> redisTemplate){
        this.redisTemplate = redisTemplate;
        String taskPoolName = scheduleProperties.getTaskPoolName();
        if (StringUtils.isBlank(taskPoolName)){
            throw new ScheduleException("The TaskPoolName cannot be empty.");
        }
        this.taskPoolName = taskPoolName;
    }

    @Override
    public void addAll(List<ScheduleTask> taskList) {
        if (CollectionUtils.isEmpty(taskList)){
            return;
        }
        Map<String, ScheduleTask> taskMap = taskList.stream().collect(Collectors.toMap(e->String.valueOf(e.getTaskId()), o -> o));
        redisTemplate.opsForHash().putAll(taskPoolName, taskMap);
    }

    @Override
    public Collection<ScheduleTask> getAll() {
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(taskPoolName);
        if (CollectionUtils.isEmpty(entries)){
            return Collections.emptyList();
        }
        List<ScheduleTask> tasks = new ArrayList<>();
        for (Object value : entries.values()) {
            tasks.add((ScheduleTask) value);
        }
        return tasks;
    }

    @Override
    public ScheduleTask get(Long taskId) {
        return (ScheduleTask) redisTemplate.opsForHash().get(taskPoolName, taskId);
    }

    @Override
    public void add(ScheduleTask scheduleTask) {
        redisTemplate.opsForHash().put(taskPoolName, scheduleTask.getTaskId(), scheduleTask);
    }

    @Override
    public void remove(Long taskId) {
        redisTemplate.opsForHash().delete(taskPoolName, taskId);
    }
}

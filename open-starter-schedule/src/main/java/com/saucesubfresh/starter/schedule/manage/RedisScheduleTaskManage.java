package com.saucesubfresh.starter.schedule.manage;

import com.saucesubfresh.starter.schedule.domain.ScheduleTask;
import com.saucesubfresh.starter.schedule.exception.ScheduleException;
import com.saucesubfresh.starter.schedule.properties.ScheduleProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 使用 redis 来管理要执行的任务
 * @author lijunping on 2022/1/20
 */
@Slf4j
public class RedisScheduleTaskManage implements ScheduleTaskManage {

    private final String taskPoolName;
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisScheduleTaskManage(ScheduleProperties scheduleProperties,
                                   RedisTemplate<String, Object> redisTemplate){
        this.redisTemplate = redisTemplate;
        String taskQueueName = scheduleProperties.getTaskQueueName();
        if (StringUtils.isBlank(taskQueueName)){
            throw new ScheduleException("The taskQueueName cannot be empty.");
        }
        this.taskPoolName = taskQueueName;
    }


    @Override
    public ScheduleTask get(Long taskId) {
        return (ScheduleTask) redisTemplate.opsForHash().get(taskPoolName, taskId);
    }

    @Override
    public List<ScheduleTask> getAll() {
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(taskPoolName);
        if (CollectionUtils.isEmpty(entries)){
            return Collections.emptyList();
        }
        return entries.values();
    }

    @Override
    public void add(ScheduleTask scheduleTask) {
        redisTemplate.opsForHash().put(taskPoolName, scheduleTask.getTaskId(), scheduleTask);
    }

    @Override
    public void remove(ScheduleTask scheduleTask) {
        redisTemplate.opsForHash().delete(taskPoolName, scheduleTask.getTaskId());
    }
}

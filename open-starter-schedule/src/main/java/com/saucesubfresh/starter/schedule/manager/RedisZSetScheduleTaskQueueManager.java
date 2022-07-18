package com.saucesubfresh.starter.schedule.manager;

import com.saucesubfresh.starter.schedule.exception.ScheduleException;
import com.saucesubfresh.starter.schedule.properties.ScheduleProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 基于 Redis ZSet 算法的分布式任务调度系统
 *
 * @author: 李俊平
 * @Date: 2022-07-10 15:27
 */
public class RedisZSetScheduleTaskQueueManager implements ScheduleTaskQueueManager{

    private final String taskQueueName;
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisZSetScheduleTaskQueueManager(ScheduleProperties scheduleProperties,
                                             RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        String taskQueueName = scheduleProperties.getTaskQueueName();
        if (StringUtils.isBlank(taskQueueName)){
            throw new ScheduleException("The TaskQueueName cannot be empty.");
        }
        this.taskQueueName = taskQueueName;
    }

    @Override
    public void put(Long taskId, Long nextTime) {
        redisTemplate.opsForZSet().add(taskQueueName, taskId, nextTime);
    }

    @Override
    public List<Long> take() {
        long nowTime = System.currentTimeMillis() / 1000;
        Set<Object> value = redisTemplate.opsForZSet().rangeByScore(taskQueueName, nowTime, nowTime);
        if (CollectionUtils.isEmpty(value)){
            return Collections.emptyList();
        }
        return value.stream().map(e->Long.valueOf(String.valueOf(e))).collect(Collectors.toList());
    }
}

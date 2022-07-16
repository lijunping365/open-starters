package com.saucesubfresh.starter.schedule.manager;

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

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisZSetScheduleTaskQueueManager(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void put(Long taskId, Long nextTime) {
        redisTemplate.opsForZSet().add("key", taskId, nextTime);
    }

    @Override
    public List<Long> take() {
        long nowTime = System.currentTimeMillis() / 1000 * 1000;
        Set<Object> value = redisTemplate.opsForZSet().rangeByScore("key", nowTime, nowTime);
        if (CollectionUtils.isEmpty(value)){
            return Collections.emptyList();
        }
        return value.stream().map(e->(Long)e).collect(Collectors.toList());
    }
}

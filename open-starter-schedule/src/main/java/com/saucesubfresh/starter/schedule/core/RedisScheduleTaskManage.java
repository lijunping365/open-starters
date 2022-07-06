package com.saucesubfresh.starter.schedule.core;

import com.saucesubfresh.starter.schedule.domain.ScheduleTask;
import com.saucesubfresh.starter.schedule.exception.ScheduleException;
import com.saucesubfresh.starter.schedule.properties.ScheduleProperties;
import com.saucesubfresh.starter.schedule.service.ScheduleTaskLoader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * 使用 redis 来管理要执行的任务
 * @author lijunping on 2022/1/20
 */
@Slf4j
public class RedisScheduleTaskManage extends AbstractScheduleTaskManage implements InitializingBean, DisposableBean {
    
    private final byte[] taskQueueName;
    private final ScheduleTaskLoader scheduleTaskLoader;
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisScheduleTaskManage(ScheduleProperties scheduleProperties,
                                   ScheduleTaskLoader scheduleTaskLoader,
                                   RedisTemplate<String, Object> redisTemplate){
        this.redisTemplate = redisTemplate;
        this.scheduleTaskLoader = scheduleTaskLoader;
        String taskQueueName = scheduleProperties.getTaskQueueName();
        if (StringUtils.isBlank(taskQueueName)){
            throw new ScheduleException("The taskQueueName cannot be empty.");
        }
        this.taskQueueName = SerializationUtils.serialize(taskQueueName);
    }

    @Override
    public void addScheduleTask(ScheduleTask scheduleTask){
        long nextTriggerTime = generateNextValidTime(scheduleTask.getCronExpression());
        redisTemplate.execute((RedisCallback<Object>) connection -> {
            byte[] value = SerializationUtils.serialize(scheduleTask);
            connection.zAdd(taskQueueName, nextTriggerTime, value);
            return null;
        });
    }

    @Override
    public void removeScheduleTask(ScheduleTask scheduleTask){
        redisTemplate.execute((RedisCallback<Object>) connection -> {
            byte[] value = SerializationUtils.serialize(scheduleTask);
            connection.zRem(taskQueueName, value);
            return null;
        });
    }

    @Override
    public List<ScheduleTask> take(long minSecond, long maxSecond){
        return redisTemplate.execute((RedisCallback<List<ScheduleTask>>) connection -> {
            Set<byte[]> tasks = connection.zRevRangeByScore(taskQueueName, minSecond, maxSecond);
            if (CollectionUtils.isEmpty(tasks)) {
                return null;
            }

            List<ScheduleTask> scheduleTasks = new ArrayList<>();
            for (byte[] value : tasks) {
                ScheduleTask scheduleTask = SerializationUtils.deserialize(value);
                long nextTriggerTime = generateNextValidTime(scheduleTask.getCronExpression());
                connection.zAdd(taskQueueName, nextTriggerTime, value);
            }
            return scheduleTasks;
        });
    }

    @Override
    public List<ScheduleTask> getScheduleTask() {
        return redisTemplate.execute((RedisCallback<List<ScheduleTask>>) connection -> {
            Set<byte[]> tasks = connection.zRange(taskQueueName, 0, -1);
            if (CollectionUtils.isEmpty(tasks)){
                return Collections.emptyList();
            }
            List<ScheduleTask> scheduleTask = new ArrayList<>();
            for (byte[] value : tasks) {
                scheduleTask.add(SerializationUtils.deserialize(value));
            }
            return scheduleTask;
        });
    }

    /**
     * 项目启动：初始化定时任务到队列
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            List<ScheduleTask> scheduleTasks = scheduleTaskLoader.loadScheduleTask();
            if (CollectionUtils.isEmpty(scheduleTasks)){
                return;
            }
            redisTemplate.execute((RedisCallback<Object>) connection -> {
                scheduleTasks.forEach(e->{
                    long nextTriggerTime = generateNextValidTime(e.getCronExpression());
                    byte[] value = SerializationUtils.serialize(e);
                    connection.zAdd(taskQueueName, nextTriggerTime, value);
                });
                return null;
            });
        }catch (Exception e){
            log.error("Schedule load task error, {}, {}", e.getMessage(), e);
        }
    }

    @Override
    public void destroy() throws Exception {
        try {
            redisTemplate.execute((RedisCallback<Object>) connection -> {
                connection.del(taskQueueName);
                return null;
            });
        }catch (Exception e){
            log.error("Schedule clear task error, {}, {}", e.getMessage(), e);
        }
    }
}

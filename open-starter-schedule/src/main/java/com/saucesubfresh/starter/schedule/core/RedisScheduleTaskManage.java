package com.saucesubfresh.starter.schedule.core;

import com.saucesubfresh.starter.schedule.domain.ScheduleTask;
import com.saucesubfresh.starter.schedule.exception.ScheduleException;
import com.saucesubfresh.starter.schedule.properties.ScheduleProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 使用 redis 来管理要执行的任务
 * @author lijunping on 2022/1/20
 */
@Slf4j
public class RedisScheduleTaskManage extends AbstractScheduleTaskManage implements InitializingBean, DisposableBean {
    
    private final byte[] taskQueueName; 
    private final byte[] taskPoolName;
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisScheduleTaskManage(ScheduleProperties scheduleProperties, 
                                   RedisTemplate<String, Object> redisTemplate){
        this.redisTemplate = redisTemplate;
        String taskQueueName = scheduleProperties.getTaskQueueName();
        String taskPoolName = scheduleProperties.getTaskPoolName();
        if (StringUtils.isBlank(taskQueueName)){
            throw new ScheduleException("The taskQueueName cannot be empty.");
        }
        if (StringUtils.isBlank(taskPoolName)){
            throw new ScheduleException("The taskPoolName cannot be empty.");
        }
        this.taskQueueName = SerializationUtils.serialize(taskQueueName);
        this.taskPoolName = SerializationUtils.serialize(taskPoolName);
    }

    @Override
    public void addScheduleTask(ScheduleTask scheduleTask){
        long nextTriggerTime = generateNextValidTime(scheduleTask.getCronExpression());
        redisTemplate.execute((RedisCallback<Object>) connection -> {
            final byte[] hField = SerializationUtils.serialize(scheduleTask.getTaskId());
            final byte[] hValue = SerializationUtils.serialize(scheduleTask);
            connection.hSet(taskPoolName, hField, hValue);
            connection.zAdd(taskQueueName, nextTriggerTime, hField);
            return null;
        });
    }

    @Override
    public void removeScheduleTask(Long taskId){
        redisTemplate.execute((RedisCallback<Object>) connection -> {
            final byte[] hField = SerializationUtils.serialize(taskId);
            connection.hDel(taskPoolName, hField);
            connection.zRem(taskQueueName, hField);
            return null;
        });
    }

    @Override
    public List<Long> takeScheduleTask(){
        Calendar instance = Calendar.getInstance();
        long nowSecond = instance.getTimeInMillis() / UNIT;
        return redisTemplate.execute((RedisCallback<List<Long>>) connection -> {
            Set<byte[]> scheduleTaskIds = connection.zRevRangeByScore(taskQueueName, nowSecond - UNIT, nowSecond);
            if (CollectionUtils.isEmpty(scheduleTaskIds)) {
                return null;
            }

            List<Long> taskList = new ArrayList<>();
            for (byte[] aByte : scheduleTaskIds) {
                final Long taskId = SerializationUtils.deserialize(aByte);
                taskList.add(taskId);
                reLoop(connection, taskId);
            }
            return taskList;
        });
    }

    @Override
    public List<ScheduleTask> getScheduleTask() throws ScheduleException {
        List<ScheduleTask> scheduleTask = new ArrayList<>();
        redisTemplate.execute((RedisCallback<Object>) connection -> {
            Map<byte[], byte[]> map = connection.hGetAll(taskPoolName);
            if (CollectionUtils.isEmpty(map)){
                return null;
            }
            for (byte[] value : map.values()) {
                scheduleTask.add(SerializationUtils.deserialize(value));
            }
            return null;
        });
        return scheduleTask;
    }

    /**
     * 重复执行该任务，在 taskId 被取出的同时判断任务池中该任务还是否存在，
     * 如果存在则计算该任务下次的执行时间并将该任务继续放入到任务执行队列中；
     * 如果任务池中不存在该任务，说明该任务已被剔除，不需要重新放入任务队列。
     * @param connection
     * @param taskId
     */
    private void reLoop(RedisConnection connection, Long taskId){
        final byte[] hField = SerializationUtils.serialize(taskId);
        final byte[] scheduleTaskByte = connection.hGet(taskPoolName, hField);
        if (Objects.isNull(scheduleTaskByte)){
            return;
        }
        final ScheduleTask scheduleTask = SerializationUtils.deserialize(scheduleTaskByte);
        long nextTriggerTime = generateNextValidTime(scheduleTask.getCronExpression());
        connection.zAdd(taskQueueName, nextTriggerTime, hField);
    }

    /**
     * 项目启动：初始化定时任务到队列
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            redisTemplate.execute((RedisCallback<Object>) connection -> {
                Map<byte[], byte[]> map = connection.hGetAll(taskPoolName);
                if (CollectionUtils.isEmpty(map)){
                    return null;
                }
                map.forEach((k, v)->{
                    final ScheduleTask scheduleTask = SerializationUtils.deserialize(v);
                    long nextTriggerTime = generateNextValidTime(scheduleTask.getCronExpression());
                    connection.zAdd(taskQueueName, nextTriggerTime, k);
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

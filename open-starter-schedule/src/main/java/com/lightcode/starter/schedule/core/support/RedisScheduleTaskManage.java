package com.lightcode.starter.schedule.core.support;

import com.lightcode.starter.schedule.core.AbstractScheduleTaskManage;
import com.lightcode.starter.schedule.domain.ScheduleTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
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
    /**
     * 任务队列名称
     */
    private static final byte[] SCHEDULE_TASK_QUEUE_KEY = SerializationUtils.serialize("schedule:task:queue:key");
    /**
     * 任务池名称
     */
    private static final byte[] SCHEDULE_TASK_POOL_KEY = SerializationUtils.serialize("schedule:task:pool:key");

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisScheduleTaskManage(RedisTemplate<String, Object> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Boolean addScheduleTask(ScheduleTask scheduleTask) {
        long nextTriggerTime = generateNextValidTime(scheduleTask.getCronExpression());
        redisTemplate.execute((RedisCallback<Object>) connection -> {
            final byte[] hField = SerializationUtils.serialize(scheduleTask.getTaskId());
            final byte[] hValue = SerializationUtils.serialize(scheduleTask);
            connection.hSet(SCHEDULE_TASK_POOL_KEY, hField, hValue);
            connection.zAdd(SCHEDULE_TASK_QUEUE_KEY, nextTriggerTime, hField);
            return null;
        });
        return true;
    }

    @Override
    public Boolean removeScheduleTask(Long taskId) {
        redisTemplate.execute((RedisCallback<Object>) connection -> {
            final byte[] hField = SerializationUtils.serialize(taskId);
            connection.hDel(SCHEDULE_TASK_POOL_KEY, hField);
            connection.zRem(SCHEDULE_TASK_QUEUE_KEY, hField);
            return null;
        });
        return true;
    }

    @Override
    public List<Long> getScheduleTaskList() {
        Calendar instance = Calendar.getInstance();
        long nowSecond = instance.getTimeInMillis() / UNIT;
        return redisTemplate.execute((RedisCallback<List<Long>>) connection -> {
            final Set<byte[]> scheduleTaskIds = connection.zRevRangeByScore(SCHEDULE_TASK_QUEUE_KEY, nowSecond - UNIT, nowSecond);
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

    /**
     * 重复执行该任务，在 taskId 被取出的同时判断任务池中该任务还是否存在，
     * 如果存在则计算该任务下次的执行时间并将该任务继续放入到任务执行队列中；
     * 如果任务池中不存在该任务，说明该任务已被剔除，不需要重新放入任务队列。
     * @param connection
     * @param taskId
     */
    private void reLoop(RedisConnection connection, Long taskId){
        final byte[] hField = SerializationUtils.serialize(taskId);
        final byte[] scheduleTaskByte = connection.hGet(SCHEDULE_TASK_POOL_KEY, hField);
        if (Objects.isNull(scheduleTaskByte)){
            return;
        }
        final ScheduleTask scheduleTask = SerializationUtils.deserialize(scheduleTaskByte);
        long nextTriggerTime = generateNextValidTime(scheduleTask.getCronExpression());
        connection.zAdd(SCHEDULE_TASK_QUEUE_KEY, nextTriggerTime, hField);
    }

    /**
     * 项目启动：初始化定时任务到队列
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        redisTemplate.execute((RedisCallback<Object>) connection -> {
            Map<byte[], byte[]> map = connection.hGetAll(SCHEDULE_TASK_POOL_KEY);
            if (CollectionUtils.isEmpty(map)){
                return null;
            }
            map.forEach((k, v)->{
                final ScheduleTask scheduleTask = SerializationUtils.deserialize(v);
                long nextTriggerTime = generateNextValidTime(scheduleTask.getCronExpression());
                connection.zAdd(SCHEDULE_TASK_QUEUE_KEY, nextTriggerTime, k);
            });
            return null;
        });
    }

    @Override
    public void destroy() throws Exception {
        redisTemplate.execute((RedisCallback<Object>) connection -> {
            connection.del(SCHEDULE_TASK_QUEUE_KEY);
            return null;
        });
    }
}

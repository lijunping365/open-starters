package com.saucesubfresh.starter.schedule.manager;

import com.saucesubfresh.starter.schedule.domain.WheelEntity;
import com.saucesubfresh.starter.schedule.exception.ScheduleException;
import com.saucesubfresh.starter.schedule.properties.ScheduleProperties;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 基于时间轮算法的分布式任务调度系统
 *
 * @author: 李俊平
 * @Date: 2022-07-10 15:27
 */
public class HashedWheelScheduleTaskQueueManager implements ScheduleTaskQueueManager{

    private final long tickDuration;
    private static final Map<Integer, Set<WheelEntity>> timeWheel = new ConcurrentHashMap<>();

    public HashedWheelScheduleTaskQueueManager(ScheduleProperties scheduleProperties){
        long tickDuration = scheduleProperties.getTickDuration();
        if (tickDuration <= 0){
            throw new ScheduleException("The TaskPoolName cannot be empty.");
        }
        this.tickDuration = tickDuration;
    }

    @Override
    public void put(Long taskId, Long nextTime) {
        long nowTime = System.currentTimeMillis() / 1000;
        if (nextTime <= nowTime){
            throw new ScheduleException("");
        }
        long diff = nextTime - nowTime;
        long round = diff / tickDuration;
        int tick = (int) (nextTime % tickDuration);

        Set<WheelEntity> taskSet = timeWheel.get(tick);
        if (CollectionUtils.isEmpty(taskSet)) {
            taskSet = new HashSet<>();
        }

        WheelEntity wheelEntity = new WheelEntity();
        wheelEntity.setRound(round);
        wheelEntity.setTaskId(taskId);
        taskSet.add(wheelEntity);
        timeWheel.put(tick, taskSet);
    }

    @Override
    public List<Long> take() {
        int nowSecond = Calendar.getInstance().get(Calendar.SECOND);
        Set<WheelEntity> entities = timeWheel.get(nowSecond);
        if (CollectionUtils.isEmpty(entities)){
            return Collections.emptyList();
        }

        Set<WheelEntity> tasks = entities.stream()
                .filter(e -> Objects.equals(e.getRound(), 0L))
                .collect(Collectors.toSet());

        if (CollectionUtils.isEmpty(tasks)){
            return Collections.emptyList();
        }

        entities.removeAll(tasks);
        for (WheelEntity entity : entities) {
            entity.setRound(entity.getRound() - 1L);
        }

        timeWheel.put(nowSecond, entities);
        return tasks.stream().map(WheelEntity::getTaskId).collect(Collectors.toList());
    }
}

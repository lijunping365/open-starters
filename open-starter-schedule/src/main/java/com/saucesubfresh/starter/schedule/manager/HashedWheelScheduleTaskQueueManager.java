package com.saucesubfresh.starter.schedule.manager;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于时间轮算法的分布式任务调度系统
 * @author: 李俊平
 * @Date: 2022-07-10 15:27
 */
public class HashedWheelScheduleTaskQueueManager implements ScheduleTaskQueueManager{

    private static final Map<Integer, List<Long>> timeWheel = new ConcurrentHashMap<>();

    @Override
    public void put(Integer key, Long taskId) {
        boolean exist = true;
        List<Long> taskList = timeWheel.get(key);
        if (CollectionUtils.isEmpty(taskList)) {
            taskList = new ArrayList<>();
            exist = false;
        }
        if (exist && taskList.contains(taskId)){
            return;
        }
        taskList.add(taskId);
        timeWheel.put(key, taskList);
    }

    @Override
    public List<Long> remove(Integer key) {
        return timeWheel.remove(key);
    }
}

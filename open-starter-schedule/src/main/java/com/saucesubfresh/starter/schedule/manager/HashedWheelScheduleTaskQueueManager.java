package com.saucesubfresh.starter.schedule.manager;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于时间轮算法的分布式任务调度系统
 *
 * @author: 李俊平
 * @Date: 2022-07-10 15:27
 */
public class HashedWheelScheduleTaskQueueManager implements ScheduleTaskQueueManager{

    private static final Map<Integer, List<Long>> timeWheel = new ConcurrentHashMap<>();

    @Override
    public void put(Long taskId, Long nextTime) {
        boolean exist = true;
        int nowSecond = Calendar.getInstance().get(Calendar.SECOND);
        List<Long> taskList = timeWheel.get(nowSecond);
        if (CollectionUtils.isEmpty(taskList)) {
            taskList = new ArrayList<>();
            exist = false;
        }
        if (exist && taskList.contains(taskId)){
            return;
        }
        taskList.add(taskId);
        timeWheel.put(nowSecond, taskList);
    }

    @Override
    public List<Long> take() {
        int nowSecond = Calendar.getInstance().get(Calendar.SECOND);
        return timeWheel.remove(nowSecond);
    }
}

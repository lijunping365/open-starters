package com.saucesubfresh.starter.schedule.manager;

import java.util.List;

/**
 * @author: 李俊平
 * @Date: 2022-07-10 15:24
 */
public interface ScheduleTaskQueueManager {

    /**
     * 任务放入到队列
     * @param key
     * @param taskId
     */
    void put(Integer key, Long taskId);

    /**
     * 将任务从队列移除并返回被移除的任务
     * @param key
     * @return
     */
    List<Long> remove(Integer key);
}

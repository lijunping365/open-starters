package com.saucesubfresh.starter.schedule.manager;

import java.util.List;

/**
 * @author: 李俊平
 * @Date: 2022-07-10 15:24
 */
public interface ScheduleTaskQueueManager {

    /**
     * 任务放入到队列
     *
     * @param taskId
     * @param nextTime
     */
    void put(Long taskId, Long nextTime);

    /**
     * 从任务队列获取任务
     *
     * @return
     */
    List<Long> take();
}

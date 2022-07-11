package com.saucesubfresh.starter.schedule.scheduler;

import com.saucesubfresh.starter.schedule.domain.ScheduleTask;
import com.saucesubfresh.starter.schedule.manager.ScheduleTaskPoolManager;
import com.saucesubfresh.starter.schedule.manager.ScheduleTaskQueueManager;

/**
 * 调度
 * @author: 李俊平
 * @Date: 2022-07-10 10:09
 */
public interface TaskProducerScheduler {

    /**
     * <p>
     *  生产任务，更新任务的下次执行时间，调用 {@link ScheduleTaskPoolManager#add(ScheduleTask)} 进行更新
     *
     *  并放入到任务队列中，调用 {@link ScheduleTaskQueueManager#put(Integer, Long)}
     * </p>
     *
     */
    void producer();
}

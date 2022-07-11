package com.saucesubfresh.starter.schedule.scheduler;

import com.saucesubfresh.starter.schedule.manager.ScheduleTaskQueueManager;

/**
 * @author: 李俊平
 * @Date: 2022-07-10 15:21
 */
public interface TaskConsumerScheduler {

    /**
     * <p>
     *  消费任务队列中的任务，通过调用 {@link ScheduleTaskQueueManager#remove(Integer)} 方法进行消费
     * </p>
     *
     */
    void consumer();
}

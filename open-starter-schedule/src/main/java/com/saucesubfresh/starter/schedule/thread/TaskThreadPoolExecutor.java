package com.saucesubfresh.starter.schedule.thread;

import com.saucesubfresh.starter.schedule.properties.ScheduleProperties;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 线程池
 */
@Slf4j
public class TaskThreadPoolExecutor extends ThreadPoolExecutor {

    public TaskThreadPoolExecutor(ScheduleProperties properties){
        super(properties.getCorePoolSize(),
                properties.getMaximumPoolSize(),
                properties.getKeepAliveTime(),
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(properties.getQueueCapacity()),
                new NamedThreadFactory(properties.getPrefix()),
                new AbortPolicy());
    }
}

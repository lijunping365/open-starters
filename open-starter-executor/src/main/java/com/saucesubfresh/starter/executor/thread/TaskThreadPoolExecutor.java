package com.saucesubfresh.starter.executor.thread;

import com.saucesubfresh.starter.executor.properties.TaskExecutorProperties;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 线程池
 */
@Slf4j
public class TaskThreadPoolExecutor extends ThreadPoolExecutor {

    public TaskThreadPoolExecutor(TaskExecutorProperties properties, BlockingQueue<Runnable> workQueue){
        super(properties.getCorePoolSize(), properties.getMaximumPoolSize(), properties.getKeepAliveTime(), TimeUnit.SECONDS, workQueue,
            new NamedThreadFactory(properties.getPrefix()),
            new AbortPolicy());
    }
}

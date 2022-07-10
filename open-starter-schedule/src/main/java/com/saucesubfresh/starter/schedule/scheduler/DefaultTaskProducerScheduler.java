package com.saucesubfresh.starter.schedule.scheduler;

import com.saucesubfresh.starter.schedule.domain.ScheduleTask;
import com.saucesubfresh.starter.schedule.manager.ScheduleTaskPoolManager;
import com.saucesubfresh.starter.schedule.manager.ScheduleTaskQueueManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 调度任务生产者，每秒执行一次
 * @author: 李俊平
 * @Date: 2022-07-10 15:23
 */
@Slf4j
public class DefaultTaskProducerScheduler implements TaskProducerScheduler, InitializingBean {
    private Thread producerScheduleThread;
    private volatile boolean producerScheduleThreadToStop = false;

    private final ScheduleTaskPoolManager scheduleTaskPoolManager;
    private final ScheduleTaskQueueManager scheduleTaskQueueManager;

    public DefaultTaskProducerScheduler(ScheduleTaskPoolManager scheduleTaskPoolManager,
                                        ScheduleTaskQueueManager scheduleTaskQueueManager) {
        this.scheduleTaskPoolManager = scheduleTaskPoolManager;
        this.scheduleTaskQueueManager = scheduleTaskQueueManager;
    }

    private void start(){
        producerScheduleThread = new Thread(()->{
            while (!producerScheduleThreadToStop) {
                //获取锁并加锁
                Collection<ScheduleTask> taskList = scheduleTaskPoolManager.getAll();
                if (CollectionUtils.isEmpty(taskList)){
                    return;
                }
                long nowTime = System.currentTimeMillis();
                long maxTime = nowTime + 5000;
                taskList = taskList.stream().filter(e->e.getNextTime() <= maxTime).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(taskList)){
                    return;
                }

                for (ScheduleTask scheduleTask : taskList) {
                    int key = (int)((scheduleTask.getNextTime()/1000)%60);
                    putTimeWheel(key, scheduleTask.getTaskId());
                    refreshNextTime(scheduleTask);
                }

                // 释放锁
                threadSleep();
            }
        });
        producerScheduleThread.setDaemon(true);
        producerScheduleThread.setName("producerScheduleThread");
        producerScheduleThread.start();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        start();
        log.info("producerScheduleThread start succeed");
    }

    private void threadSleep(){
        try {
            TimeUnit.MILLISECONDS.sleep(1000 - System.currentTimeMillis() % 1000);
        } catch (InterruptedException e) {
            if (!producerScheduleThreadToStop) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private void putTimeWheel(int key, long taskId){
        scheduleTaskQueueManager.put(key, taskId);
    }

    private void refreshNextTime(ScheduleTask scheduleTask){
        scheduleTaskPoolManager.add(scheduleTask);
    }
}

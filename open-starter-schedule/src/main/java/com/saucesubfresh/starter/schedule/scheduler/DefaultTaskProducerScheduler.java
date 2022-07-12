package com.saucesubfresh.starter.schedule.scheduler;

import com.saucesubfresh.starter.schedule.domain.ScheduleTask;
import com.saucesubfresh.starter.schedule.manager.ScheduleTaskPoolManager;
import com.saucesubfresh.starter.schedule.manager.ScheduleTaskQueueManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 调度任务生产者
 * @author: 李俊平
 * @Date: 2022-07-10 15:23
 */
@Slf4j
public class DefaultTaskProducerScheduler implements TaskProducerScheduler, InitializingBean, DisposableBean {
    private Thread producerScheduleThread;
    public static final long PRE_READ_MS = 1000;
    private volatile boolean producerScheduleThreadToStop = false;
    private final ScheduleTaskPoolManager scheduleTaskPoolManager;
    private final ScheduleTaskQueueManager scheduleTaskQueueManager;

    public DefaultTaskProducerScheduler(ScheduleTaskPoolManager scheduleTaskPoolManager,
                                        ScheduleTaskQueueManager scheduleTaskQueueManager) {
        this.scheduleTaskPoolManager = scheduleTaskPoolManager;
        this.scheduleTaskQueueManager = scheduleTaskQueueManager;
    }

    @Override
    public void producer() {
        try {
            //获取锁并加锁
            Collection<ScheduleTask> taskList = scheduleTaskPoolManager.getAll();
            if (CollectionUtils.isEmpty(taskList)){
                return;
            }
            long nowTime = System.currentTimeMillis();
            log.info("生产线程当前时间: {}", nowTime);
            taskList = taskList.stream()
                    .filter(e->e.getNextTime() >= nowTime && e.getNextTime() < nowTime + PRE_READ_MS)
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(taskList)){
                return;
            }
            for (ScheduleTask scheduleTask : taskList) {
                int key = (int)((scheduleTask.getNextTime() / 1000) % 60);
                putTimeWheel(key, scheduleTask.getTaskId());
                log.info("producer schedule task key:{}, result {}", key, scheduleTask);
            }
        } catch (Exception e){
            if (!producerScheduleThreadToStop) {
                log.error("producerScheduleThread error:{}", e.getMessage(), e);
            }
        } finally {
            // 释放锁
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        start();
        log.info("producerScheduleThread start succeed");
    }

    @Override
    public void destroy() throws Exception {
        producerScheduleThreadToStop = true;
        stopThread(producerScheduleThread);
        log.info("producerScheduleThread stop success");
    }

    private void start(){
        producerScheduleThread = new Thread(()->{
            while (!producerScheduleThreadToStop) {
                threadSleep();
                this.producer();
            }
            log.info("producerScheduleThread stop");
        });
        producerScheduleThread.setDaemon(true);
        producerScheduleThread.setName("producerScheduleThread");
        producerScheduleThread.start();
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

    /**
     * 放入到时间轮（任务队列）中
     * @param key
     * @param taskId
     */
    private void putTimeWheel(int key, long taskId){
        scheduleTaskQueueManager.put(key, taskId);
    }

    /**
     * 停止线程
     * @param thread 要停止的线程
     */
    private void stopThread(Thread thread){
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }

        if (thread.getState() == Thread.State.TERMINATED){
            return;
        }

        // interrupt and wait
        thread.interrupt();
        try {
            thread.join();
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }
}

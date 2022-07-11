package com.saucesubfresh.starter.schedule.scheduler;

import com.saucesubfresh.starter.schedule.executor.ScheduleTaskExecutor;
import com.saucesubfresh.starter.schedule.manager.ScheduleTaskQueueManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 调度任务消费者
 * @author: 李俊平
 * @Date: 2022-07-10 15:23
 */
@Slf4j
public class DefaultTaskConsumerScheduler implements TaskConsumerScheduler, InitializingBean, DisposableBean {

    private Thread consumerScheduleThread;
    private volatile boolean consumerScheduleThreadToStop = false;

    private final ScheduleTaskExecutor scheduleTaskExecutor;
    private final ScheduleTaskQueueManager scheduleTaskQueueManager;


    public DefaultTaskConsumerScheduler(ScheduleTaskExecutor scheduleTaskExecutor,
                                        ScheduleTaskQueueManager scheduleTaskQueueManager) {
        this.scheduleTaskExecutor = scheduleTaskExecutor;
        this.scheduleTaskQueueManager = scheduleTaskQueueManager;
    }

    @Override
    public void consumer() {
        try {
            int nowSecond = Calendar.getInstance().get(Calendar.SECOND);
            List<Long> taskIds = takeTask((nowSecond) % 60);
            log.info("load schedule task key:{}, result {}", (nowSecond) % 60, taskIds);
            if (CollectionUtils.isEmpty(taskIds)){
                return;
            }
            trigger(taskIds);
            taskIds.clear();
        }catch (Exception e){
            if (!consumerScheduleThreadToStop) {
                log.error("consumerScheduleThread error:{}", e.getMessage(), e);
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        start();
        log.info("consumerScheduleThread start succeed");
    }

    @Override
    public void destroy() throws Exception {
        consumerScheduleThreadToStop = true;
        stopThread(consumerScheduleThread);
        log.info("consumerScheduleThread stop success");
    }

    public void start(){
        consumerScheduleThread = new Thread(()->{
            while (!consumerScheduleThreadToStop) {
                threadSleep();
                this.consumer();
            }
            log.info("consumerScheduleThread stop");
        });
        consumerScheduleThread.setDaemon(true);
        consumerScheduleThread.setName("consumerScheduleThread");
        consumerScheduleThread.start();
    }

    private void threadSleep(){
        try {
            TimeUnit.MILLISECONDS.sleep(1000 - System.currentTimeMillis() % 1000);
        } catch (InterruptedException e) {
            if (!consumerScheduleThreadToStop) {
                log.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 从时间轮（任务队列）中取出对应的任务列表
     * @param key
     * @return
     */
    private List<Long> takeTask(int key){
        return scheduleTaskQueueManager.remove(key);
    }

    /**
     * 触发任务执行
     * @param taskIds
     */
    private void trigger(List<Long> taskIds){
        scheduleTaskExecutor.execute(taskIds);
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

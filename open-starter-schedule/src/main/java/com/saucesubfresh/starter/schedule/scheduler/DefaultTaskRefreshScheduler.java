package com.saucesubfresh.starter.schedule.scheduler;

import com.saucesubfresh.starter.schedule.domain.ScheduleTask;
import com.saucesubfresh.starter.schedule.manager.ScheduleTaskPoolManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 调度任务消费者
 * @author: 李俊平
 * @Date: 2022-07-10 15:23
 */
@Slf4j
public class DefaultTaskRefreshScheduler implements TaskRefreshScheduler, InitializingBean, DisposableBean {

    private Thread refreshScheduleThread;
    private volatile boolean refreshScheduleThreadToStop = false;
    private final ScheduleTaskPoolManager scheduleTaskPoolManager;

    public DefaultTaskRefreshScheduler(ScheduleTaskPoolManager scheduleTaskPoolManager) {
        this.scheduleTaskPoolManager = scheduleTaskPoolManager;
    }

    @Override
    public void refresh() {
        try {
            //获取锁并加锁
            Collection<ScheduleTask> taskList = scheduleTaskPoolManager.getAll();
            if (CollectionUtils.isEmpty(taskList)){
                return;
            }
            long nowTime = System.currentTimeMillis();
            log.info("刷新线程当前时间: {}", nowTime);
            taskList = taskList.stream().filter(e->e.getNextTime() <= nowTime).collect(Collectors.toList());

            if (CollectionUtils.isEmpty(taskList)){
                return;
            }
            for (ScheduleTask scheduleTask : taskList) {
                refreshNextTime(scheduleTask);
                log.info("refresh schedule task {}", scheduleTask);
            }
        }catch (Exception e){
            if (!refreshScheduleThreadToStop) {
                log.error("refreshScheduleThread error:{}", e.getMessage(), e);
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        start();
        log.info("refreshScheduleThread start succeed");
    }

    @Override
    public void destroy() throws Exception {
        refreshScheduleThreadToStop = true;
        stopThread(refreshScheduleThread);
        log.info("refreshScheduleThread stop success");
    }

    public void start(){
        refreshScheduleThread = new Thread(()->{
            while (!refreshScheduleThreadToStop) {
                threadSleep();
                this.refresh();
            }
            log.info("refreshScheduleThread stop");
        });
        refreshScheduleThread.setDaemon(true);
        refreshScheduleThread.setName("refreshScheduleThread");
        refreshScheduleThread.start();
    }

    private void threadSleep(){
        try {
            TimeUnit.MILLISECONDS.sleep(1000 - System.currentTimeMillis() % 1000);
        } catch (InterruptedException e) {
            if (!refreshScheduleThreadToStop) {
                log.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 更新任务的下次执行时间
     * @param scheduleTask
     */
    private void refreshNextTime(ScheduleTask scheduleTask){
        scheduleTaskPoolManager.add(scheduleTask);
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

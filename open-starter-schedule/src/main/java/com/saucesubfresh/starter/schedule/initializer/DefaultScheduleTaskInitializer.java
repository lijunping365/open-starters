package com.saucesubfresh.starter.schedule.initializer;

import com.saucesubfresh.starter.schedule.cron.CronHelper;
import com.saucesubfresh.starter.schedule.domain.ScheduleTask;
import com.saucesubfresh.starter.schedule.loader.ScheduleTaskLoader;
import com.saucesubfresh.starter.schedule.manager.ScheduleTaskPoolManager;
import com.saucesubfresh.starter.schedule.manager.ScheduleTaskQueueManager;
import com.saucesubfresh.starter.schedule.TaskJobScheduler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author: 李俊平
 * @Date: 2022-07-08 23:17
 */
@Slf4j
public class DefaultScheduleTaskInitializer implements ScheduleTaskInitializer, InitializingBean, DisposableBean {

    private final TaskJobScheduler taskJobScheduler;
    private final ScheduleTaskLoader scheduleTaskLoader;
    private final ScheduleTaskPoolManager scheduleTaskPoolManager;
    private final ScheduleTaskQueueManager scheduleTaskQueueManager;

    public DefaultScheduleTaskInitializer(TaskJobScheduler taskJobScheduler,
                                          ScheduleTaskLoader scheduleTaskLoader,
                                          ScheduleTaskPoolManager scheduleTaskPoolManager,
                                          ScheduleTaskQueueManager scheduleTaskQueueManager) {
        this.taskJobScheduler = taskJobScheduler;
        this.scheduleTaskLoader = scheduleTaskLoader;
        this.scheduleTaskPoolManager = scheduleTaskPoolManager;
        this.scheduleTaskQueueManager = scheduleTaskQueueManager;
    }

    @Override
    public void initialize() {
        List<ScheduleTask> scheduleTasks = scheduleTaskLoader.loadScheduleTask();
        if (!CollectionUtils.isEmpty(scheduleTasks)){
            scheduleTaskPoolManager.addAll(scheduleTasks);
            for (ScheduleTask task : scheduleTasks) {
                long nextTime = CronHelper.getNextTime(task.getCronExpression());
                scheduleTaskQueueManager.put(task.getTaskId(), nextTime);
            }
        }
        taskJobScheduler.start();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            initialize();
            log.info("Schedule task initialize succeed");
        }catch (Exception e){
            log.error("Schedule task initialize failed, {}", e.getMessage());
        }
    }

    @Override
    public void destroy() throws Exception {
        taskJobScheduler.stop();
    }
}

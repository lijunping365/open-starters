package com.saucesubfresh.starter.schedule.initializer;

import com.saucesubfresh.starter.schedule.domain.ScheduleTask;
import com.saucesubfresh.starter.schedule.loader.ScheduleTaskLoader;
import com.saucesubfresh.starter.schedule.manager.ScheduleTaskPoolManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;

/**
 * @author: 李俊平
 * @Date: 2022-07-08 23:17
 */
@Slf4j
public class DefaultScheduleTaskInitializer implements ScheduleTaskInitializer, InitializingBean {

    private final ScheduleTaskLoader scheduleTaskLoader;
    private final ScheduleTaskPoolManager scheduleTaskPoolManager;

    public DefaultScheduleTaskInitializer(ScheduleTaskLoader scheduleTaskLoader,
                                          ScheduleTaskPoolManager scheduleTaskPoolManager) {
        this.scheduleTaskLoader = scheduleTaskLoader;
        this.scheduleTaskPoolManager = scheduleTaskPoolManager;
    }

    @Override
    public void initialize() {
        List<ScheduleTask> scheduleTasks = scheduleTaskLoader.loadScheduleTask();
        scheduleTaskPoolManager.addAll(scheduleTasks);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            initialize();
            log.info("Schedule task initialize successed");
        }catch (Exception e){
            log.error("Schedule task initialize failed, {}", e.getMessage());
        }
    }
}

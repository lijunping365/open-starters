package com.saucesubfresh.starter.schedule.trigger;

import com.saucesubfresh.starter.schedule.core.ScheduleTaskManage;
import com.saucesubfresh.starter.schedule.exception.ScheduleException;
import com.saucesubfresh.starter.schedule.executor.ScheduleTaskExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author lijunping on 2022/3/31
 */
@Slf4j
public abstract class AbstractScheduleTaskTrigger implements ScheduleTaskTrigger, DisposableBean {

    private final ThreadPoolExecutor executor;
    private final ScheduleTaskManage scheduleTaskManage;
    private final ScheduleTaskExecutor scheduleTaskExecutor;

    public AbstractScheduleTaskTrigger(ThreadPoolExecutor executor,
                                       ScheduleTaskManage scheduleTaskManage,
                                       ScheduleTaskExecutor scheduleTaskExecutor) {
        this.executor = executor;
        this.scheduleTaskManage = scheduleTaskManage;
        this.scheduleTaskExecutor = scheduleTaskExecutor;
    }

    @Override
    public void trigger() {
        executor.execute(()->{
            List<Long> scheduleTaskList = null;
            try {
                scheduleTaskList = scheduleTaskManage.takeScheduleTask();
            }catch (Exception e){
                log.error(e.getMessage(), e);
            }
            if (CollectionUtils.isEmpty(scheduleTaskList)){
                return;
            }
            scheduleTaskExecutor.execute(scheduleTaskList);
        });
    }

    @Override
    public void destroy() {
        executor.shutdown();
    }
}

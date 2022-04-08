package com.saucesubfresh.starter.schedule.trigger;

import com.saucesubfresh.starter.schedule.core.ScheduleTaskManage;
import com.saucesubfresh.starter.schedule.executor.ScheduleTaskExecutor;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author lijunping on 2022/3/31
 */
public abstract class AbstractScheduleTaskTrigger implements ScheduleTaskTrigger{

    private final ScheduleTaskManage scheduleTaskManage;
    protected final ScheduleTaskExecutor scheduleTaskExecutor;

    public AbstractScheduleTaskTrigger(ScheduleTaskManage scheduleTaskManage, ScheduleTaskExecutor scheduleTaskExecutor) {
        this.scheduleTaskManage = scheduleTaskManage;
        this.scheduleTaskExecutor = scheduleTaskExecutor;
    }

    @Override
    public void trigger() {
        List<Long> scheduleTaskList = scheduleTaskManage.getScheduleTaskList();
        if (CollectionUtils.isEmpty(scheduleTaskList)){
            return;
        }
        scheduleTaskExecutor.execute(scheduleTaskList);
    }
}

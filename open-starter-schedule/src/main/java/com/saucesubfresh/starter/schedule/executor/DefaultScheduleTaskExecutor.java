package com.saucesubfresh.starter.schedule.executor;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author lijunping on 2022/1/21
 */
@Slf4j
public class DefaultScheduleTaskExecutor implements ScheduleTaskExecutor{

    @Override
    public void execute(List<Long> taskList) {
        log.info("execute task {}", taskList);
    }
}

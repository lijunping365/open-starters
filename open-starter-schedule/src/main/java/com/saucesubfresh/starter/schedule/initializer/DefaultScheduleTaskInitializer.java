/*
 * Copyright © 2022 organization SauceSubFresh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.saucesubfresh.starter.schedule.initializer;

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
 * @author lijunping
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
                scheduleTaskQueueManager.put(task.getTaskId(), task.getCronExpression());
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

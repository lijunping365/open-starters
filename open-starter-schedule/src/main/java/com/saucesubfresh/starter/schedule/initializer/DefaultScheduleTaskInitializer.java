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

import com.saucesubfresh.starter.schedule.TaskJobScheduler;
import com.saucesubfresh.starter.schedule.domain.ScheduleTask;
import com.saucesubfresh.starter.schedule.service.TaskService;
import com.saucesubfresh.starter.schedule.wheel.TimeWheel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

/**
 * @author lijunping
 */
@Slf4j
public class DefaultScheduleTaskInitializer implements ScheduleTaskInitializer, InitializingBean, DisposableBean {

    private final TimeWheel timeWheel;
    private final TaskService taskService;
    private final TaskJobScheduler taskJobScheduler;

    public DefaultScheduleTaskInitializer(TimeWheel timeWheel,
                                          TaskService taskService,
                                          TaskJobScheduler taskJobScheduler) {
        this.timeWheel = timeWheel;
        this.taskService = taskService;
        this.taskJobScheduler = taskJobScheduler;
    }

    @Override
    public void initialize() {
        loadTask();
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

    private void loadTask(){
        Collection<ScheduleTask> scheduleTasks = taskService.loadTask();
        if (CollectionUtils.isEmpty(scheduleTasks)){
            return;
        }

        for (ScheduleTask task : scheduleTasks) {
            timeWheel.put(task.getTaskId(), task.getCronExpression());
        }
    }
}
